package net.minecraft.client.renderer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.resource.RenderTargetDescriptor;
import com.mojang.blaze3d.resource.RenderTargetDescriptor$TextureProperties;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout$Builder;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline$Builder;
import com.mojang.renderpearl.api.pipeline.UniformType;
import java.lang.runtime.SwitchBootstraps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

@Environment(EnvType.CLIENT)
public class PostChain implements AutoCloseable {
   public static final Identifier MAIN_TARGET_ID = Identifier.withDefaultNamespace("main");
   private final Identifier id;
   private final List<PostPass> passes;
   private final Map<Identifier, PostChainConfig$InternalTarget> internalTargets;
   private final Set<Identifier> externalTargets;
   private final Map<Identifier, RenderTarget> persistentTargets = new HashMap<>();
   private final Projection projection;
   private final ProjectionMatrixBuffer projectionMatrixBuffer;

   private PostChain(
      final Identifier id,
      final List<PostPass> passes,
      final Map<Identifier, PostChainConfig$InternalTarget> internalTargets,
      final Set<Identifier> externalTargets,
      final Projection projection,
      final ProjectionMatrixBuffer projectionMatrixBuffer
   ) {
      this.id = id;
      this.passes = passes;
      this.internalTargets = internalTargets;
      this.externalTargets = externalTargets;
      this.projection = projection;
      this.projectionMatrixBuffer = projectionMatrixBuffer;
   }

   public Identifier id() {
      return this.id;
   }

   public static PostChain load(
      final PostChainConfig config,
      final TextureManager textureManager,
      final Set<Identifier> allowedExternalTargets,
      final Identifier id,
      final Projection projection,
      final ProjectionMatrixBuffer projectionMatrixBuffer
   ) throws ShaderManager$CompilationException {
      Set<Identifier> referencedExternalTargets = getReferencedExternalTargets(config);
      Set<Identifier> invalidExternalTargets = Sets.difference(referencedExternalTargets, allowedExternalTargets);
      if (!invalidExternalTargets.isEmpty()) {
         throw new ShaderManager$CompilationException("Referenced external targets are not available in this context: " + invalidExternalTargets);
      } else {
         Builder<PostPass> passes = ImmutableList.builder();

         for (int i = 0; i < config.passes().size(); i++) {
            PostChainConfig$Pass pass = config.passes().get(i);
            passes.add(createPass(textureManager, pass, id.withSuffix("/" + i)));
         }

         return new PostChain(id, passes.build(), config.internalTargets(), referencedExternalTargets, projection, projectionMatrixBuffer);
      }
   }

   public static Set<Identifier> getReferencedExternalTargets(final PostChainConfig config) {
      Stream<Identifier> referencedTargets = config.passes().stream().flatMap(PostChainConfig$Pass::referencedTargets);
      return referencedTargets.filter(targetId -> !config.internalTargets().containsKey(targetId)).collect(Collectors.toSet());
   }

   private static PostPass createPass(final TextureManager textureManager, final PostChainConfig$Pass config, final Identifier id) throws ShaderManager$CompilationException {
      RenderPipeline$Builder pipelineBuilder = RenderPipeline.builder(RenderPipelines.POST_PROCESSING_SNIPPET)
         .withFragmentShader(config.fragmentShaderId())
         .withVertexShader(config.vertexShaderId())
         .withLocation(id);
      BindGroupLayout$Builder bindGroupLayoutBuilder = BindGroupLayout.builder();

      for (PostChainConfig$Input input : config.inputs()) {
         bindGroupLayoutBuilder.withUniform(input.samplerName() + "Sampler", UniformType.COMBINED_IMAGE_SAMPLER);
      }

      bindGroupLayoutBuilder.withUniform("SamplerInfo", UniformType.UNIFORM_BUFFER);

      for (String uniformGroupName : config.uniforms().keySet()) {
         bindGroupLayoutBuilder.withUniform(uniformGroupName, UniformType.UNIFORM_BUFFER);
      }

      pipelineBuilder.withBindGroupLayout(bindGroupLayoutBuilder.build());
      pipelineBuilder.withColorTargetState(ColorTargetState.DEFAULT);
      RenderPipeline pipeline = pipelineBuilder.build();
      if (RenderSystem.getCompiledPipelineNullable(pipeline) == null) {
         throw new ShaderManager$CompilationException("Failed to compile post processing pipeline " + pipeline.getLocation());
      } else {
         List<PostPass$Input> inputs = new ArrayList<>();

         label114:
         for (PostChainConfig$Input input : config.inputs()) {
            Objects.requireNonNull(input);
            PostChainConfig$Input var9 = input;
            byte var10 = 0;

            while (true) {
               switch (SwitchBootstraps.typeSwitch<"typeSwitch",PostChainConfig$TextureInput,PostChainConfig$TargetInput>(var9, var10)) {
                  case 0:
                     PostChainConfig$TextureInput var11 = (PostChainConfig$TextureInput)var9;
                     PostChainConfig$TextureInput var53 = var11;

                     try {
                        var54 = var53.samplerName();
                     } catch (Throwable var32) {
                        throw new MatchException(var32.toString(), var32);
                     }

                     String var37 = var54;
                     var53 = var11;

                     try {
                        var56 = var53.location();
                     } catch (Throwable var31) {
                        throw new MatchException(var31.toString(), var31);
                     }

                     Identifier var38 = var56;
                     var53 = var11;

                     try {
                        var58 = var53.width();
                     } catch (Throwable var30) {
                        throw new MatchException(var30.toString(), var30);
                     }

                     int var39 = var58;
                     if (true) {
                        var53 = var11;

                        try {
                           var60 = var53.height();
                        } catch (Throwable var29) {
                           throw new MatchException(var29.toString(), var29);
                        }

                        int var40 = var60;
                        if (true) {
                           var53 = var11;

                           try {
                              var62 = var53.bilinear();
                           } catch (Throwable var28) {
                              throw new MatchException(var28.toString(), var28);
                           }

                           boolean var41 = var62;
                           if (true) {
                              AbstractTexture var42 = textureManager.getTexture(var38.withPath(path -> "textures/effect/" + path + ".png"));
                              inputs.add(new PostPass$TextureInput(var37, var42, var39, var40, var41));
                              continue label114;
                           }
                        }
                     }

                     var10 = 1;
                     break;
                  case 1:
                     PostChainConfig$TargetInput texture = (PostChainConfig$TargetInput)var9;
                     PostChainConfig$TargetInput var10000 = texture;

                     try {
                        var46 = var10000.samplerName();
                     } catch (Throwable var27) {
                        throw new MatchException(var27.toString(), var27);
                     }

                     String var22 = var46;
                     var10000 = texture;

                     try {
                        var48 = var10000.targetId();
                     } catch (Throwable var26) {
                        throw new MatchException(var26.toString(), var26);
                     }

                     Identifier var43 = var48;
                     var10000 = texture;

                     try {
                        var50 = var10000.useDepthBuffer();
                     } catch (Throwable var25) {
                        throw new MatchException(var25.toString(), var25);
                     }

                     boolean var44 = var50;
                     if (true) {
                        var10000 = texture;

                        try {
                           var52 = var10000.bilinear();
                        } catch (Throwable var24) {
                           throw new MatchException(var24.toString(), var24);
                        }

                        boolean var45 = var52;
                        if (true) {
                           inputs.add(new PostPass$TargetInput(var22, var43, var44, var45));
                           continue label114;
                        }
                     }

                     var10 = 2;
                     break;
                  default:
                     throw new MatchException(null, null);
               }
            }
         }

         return new PostPass(pipeline, config.outputTarget(), config.uniforms(), inputs);
      }
   }

   public void addToFrame(final FrameGraphBuilder frame, final int screenWidth, final int screenHeight, final PostChain$TargetBundle providedTargets) {
      this.projection.setSize(screenWidth, screenHeight);
      GpuBufferSlice projectionBuffer = this.projectionMatrixBuffer.getBuffer(this.projection);
      Map<Identifier, ResourceHandle<RenderTarget>> targets = new HashMap<>(this.internalTargets.size() + this.externalTargets.size());

      for (Identifier id : this.externalTargets) {
         targets.put(id, providedTargets.getOrThrow(id));
      }

      for (Entry<Identifier, PostChainConfig$InternalTarget> entry : this.internalTargets.entrySet()) {
         Identifier id = entry.getKey();
         PostChainConfig$InternalTarget target = entry.getValue();
         RenderTargetDescriptor descriptor = new RenderTargetDescriptor(
            target.width().orElse(screenWidth),
            target.height().orElse(screenHeight),
            new RenderTargetDescriptor$TextureProperties(ARGB.vector4fFromARGB32(target.clearColor()), GpuFormat.RGBA8_UNORM),
            RenderTargetDescriptor$TextureProperties.DEFAULT_DEPTH
         );
         if (target.persistent()) {
            RenderTarget persistentTarget = this.getOrCreatePersistentTarget(id, descriptor);
            targets.put(id, frame.importExternal(id.toString(), persistentTarget));
         } else {
            targets.put(id, frame.createInternal(id.toString(), descriptor));
         }
      }

      for (PostPass pass : this.passes) {
         pass.addToFrame(frame, targets, projectionBuffer);
      }

      for (Identifier id : this.externalTargets) {
         providedTargets.replace(id, targets.get(id));
      }
   }

   @Deprecated
   public void process(final RenderTarget mainTarget, final GraphicsResourceAllocator resourceAllocator) {
      FrameGraphBuilder frame = new FrameGraphBuilder();
      PostChain$TargetBundle targets = PostChain$TargetBundle.of(MAIN_TARGET_ID, frame.importExternal("main", mainTarget));
      this.addToFrame(frame, mainTarget.width, mainTarget.height, targets);
      frame.execute(resourceAllocator);
   }

   private RenderTarget getOrCreatePersistentTarget(final Identifier id, final RenderTargetDescriptor descriptor) {
      RenderTarget target = this.persistentTargets.get(id);
      if (target == null || target.width != descriptor.width() || target.height != descriptor.height()) {
         if (target != null) {
            target.destroyBuffers();
         }

         target = descriptor.allocate();
         descriptor.prepare(target);
         this.persistentTargets.put(id, target);
      }

      return target;
   }

   public void closePersistentTargets() {
      this.persistentTargets.values().forEach(RenderTarget::destroyBuffers);
      this.persistentTargets.clear();
   }

   @Override
   public void close() {
      this.closePersistentTargets();

      for (PostPass pass : this.passes) {
         pass.close();
      }
   }
}
