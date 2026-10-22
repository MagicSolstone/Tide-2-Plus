package net.minecraft.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.pipeline.PipelineCache;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline;
import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline$Pending;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.ShaderSource;
import com.mojang.renderpearl.api.pipeline.ShaderSource$CachedIncludeSource;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import com.mojang.serialization.JsonOps;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.PreparableReloadListener$PreparationBarrier;
import net.minecraft.server.packs.resources.PreparableReloadListener$SharedState;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class ShaderManager implements PreparableReloadListener, AutoCloseable {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final int MAX_LOG_LENGTH = 32768;
   public static final String SHADER_PATH = "shaders";
   public static final String SHADER_INCLUDE_PATH = "shaders/include/";
   public static final String SHADER_INCLUDE_EXTENSION = ".glsl";
   public static final FileToIdConverter SHADER_INCLUDE_CONVERTER = new FileToIdConverter("shaders/include", ".glsl");
   private static final FileToIdConverter POST_CHAIN_ID_CONVERTER = FileToIdConverter.json("post_effect");
   private final TextureManager textureManager;
   private final Consumer<Exception> recoveryHandler;
   private ShaderManager$PostChainCache postChains = new ShaderManager$PostChainCache(this, ShaderManager$Configs.EMPTY);
   private final Projection postChainProjection = new Projection();
   private final ProjectionMatrixBuffer postChainProjectionMatrixBuffer = new ProjectionMatrixBuffer("post");

   public ShaderManager(final TextureManager textureManager, final Consumer<Exception> recoveryHandler) {
      this.textureManager = textureManager;
      this.recoveryHandler = recoveryHandler;
      this.postChainProjection.setupOrtho(0.1F, 1000.0F, 1.0F, 1.0F, false);
   }

   @Override
   public final CompletableFuture<Void> reload(
      final PreparableReloadListener$SharedState currentReload,
      final Executor taskExecutor,
      final PreparableReloadListener$PreparationBarrier preparationBarrier,
      final Executor reloadExecutor
   ) {
      ResourceManager manager = currentReload.resourceManager();
      GpuDevice device = RenderSystem.getDevice();
      return CompletableFuture.<ShaderManager$Configs>supplyAsync(() -> loadConfigs(manager), taskExecutor)
         .thenComposeAsync(
            configs -> {
               List<RenderPipeline> requiredPipelines = RenderPipelines.requiredPipelines();
               List<RenderPipeline> optionalPipelines = RenderPipelines.optionalPipelines();
               return compilePipelines(device, configs, requiredPipelines, taskExecutor, reloadExecutor)
                  .thenCombine(
                     compilePipelines(device, configs, optionalPipelines, taskExecutor, reloadExecutor),
                     (compiledRequiredPipelines, compiledOptionalPipelines) -> new ShaderManager$PendingResults(
                        configs,
                        requiredPipelines,
                        optionalPipelines,
                        (List<CompiledRenderPipeline>)compiledRequiredPipelines,
                        (List<CompiledRenderPipeline>)compiledOptionalPipelines
                     )
                  );
            },
            reloadExecutor
         )
         .thenCompose(preparationBarrier::wait)
         .thenAcceptAsync(compilations -> this.apply(device, compilations), reloadExecutor);
   }

   private static CompletableFuture<List<CompiledRenderPipeline>> compilePipelines(
      final GpuDevice device, final ShaderSource shaderSource, final List<RenderPipeline> pipelines, final Executor taskExecutor, final Executor reloadExecutor
   ) {
      return Util.sequence(
         pipelines.stream()
            .map(
               pipeline -> device.compilePipeline(pipeline, shaderSource, taskExecutor)
                  .thenApplyAsync(CompiledRenderPipeline$Pending::finishCompile, reloadExecutor)
            )
            .toList()
      );
   }

   private static ShaderManager$Configs loadConfigs(final ResourceManager manager) {
      Builder<ShaderManager$ShaderSourceKey, String> shaderSources = ImmutableMap.builder();
      Builder<Identifier, ShaderSource$CachedIncludeSource> includeSources = ImmutableMap.builder();
      Map<Identifier, Resource> files = manager.listResources("shaders", var0 -> true);

      for (Entry<Identifier, Resource> entry : files.entrySet()) {
         Identifier location = entry.getKey();
         ShaderType shaderType = ShaderType.byLocation(location);
         if (shaderType != null) {
            loadShader(location, entry.getValue(), shaderType, shaderSources);
         } else if (SHADER_INCLUDE_CONVERTER.matches(location)) {
            loadInclude(location, entry.getValue(), includeSources);
         }
      }

      Builder<Identifier, PostChainConfig> postChains = ImmutableMap.builder();

      for (Entry<Identifier, Resource> entryx : POST_CHAIN_ID_CONVERTER.listMatchingResources(manager).entrySet()) {
         loadPostChain(entryx.getKey(), entryx.getValue(), postChains);
      }

      return new ShaderManager$Configs(shaderSources.build(), includeSources.build(), postChains.build());
   }

   private static void loadShader(
      final Identifier location, final Resource resource, final ShaderType type, final Builder<ShaderManager$ShaderSourceKey, String> output
   ) {
      try {
         String contents = resource.readAllAsString();
         Identifier id = type.idConverter().fileToId(location);
         output.put(new ShaderManager$ShaderSourceKey(id, type), contents);
      } catch (IOException var6) {
         LOGGER.error("Failed to load shader source at {}", location, var6);
      }
   }

   private static void loadInclude(final Identifier location, final Resource resource, final Builder<Identifier, ShaderSource$CachedIncludeSource> output) {
      try {
         String contents = resource.readAllAsString();
         Identifier id = includeShaderLocationToId(location);
         output.put(id, ShaderSource$CachedIncludeSource.create(id, contents));
      } catch (IOException var5) {
         LOGGER.error("Failed to load shader source at {}", location, var5);
      }
   }

   private static void loadPostChain(final Identifier location, final Resource resource, final Builder<Identifier, PostChainConfig> output) {
      Identifier id = POST_CHAIN_ID_CONVERTER.fileToId(location);

      try (Reader reader = resource.openAsReader()) {
         JsonElement json = StrictJsonParser.parse(reader);
         output.put(id, (PostChainConfig)PostChainConfig.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
      } catch (JsonParseException | IOException var9) {
         LOGGER.error("Failed to parse post chain at {}", location, var9);
      }
   }

   public static Map<Identifier, ShaderSource$CachedIncludeSource> listAllIncludes(final ResourceManager resourceManager) {
      Map<Identifier, ShaderSource$CachedIncludeSource> includes = new HashMap<>();
      SHADER_INCLUDE_CONVERTER.listMatchingResources(resourceManager).forEach((location, resource) -> {
         try {
            String contents = resource.readAllAsString();
            Identifier includeId = includeShaderLocationToId(location);
            includes.put(includeId, ShaderSource$CachedIncludeSource.create(includeId, contents));
         } catch (Exception var5) {
            LOGGER.error("Couldn't read shader file {}", location, var5);
         }
      });
      return includes;
   }

   private static Identifier includeShaderLocationToId(final Identifier location) {
      return SHADER_INCLUDE_CONVERTER.fileToId(location).withSuffix(".glsl");
   }

   private void apply(final GpuDevice device, final ShaderManager$PendingResults compilations) {
      ShaderManager$PostChainCache newPostChains = new ShaderManager$PostChainCache(this, compilations.configs);
      List<Identifier> failedLoads = new ArrayList<>();
      PipelineCache pipelineCache = new PipelineCache(device, compilations.configs);
      pipelineCache.clear();

      for (int i = 0; i < compilations.requiredPipelines.size(); i++) {
         RenderPipeline pipeline = compilations.requiredPipelines.get(i);
         CompiledRenderPipeline compiled = compilations.compiledRequiredPipelines.get(i);
         if (compiled != null) {
            pipelineCache.insert(pipeline, compiled);
         } else {
            failedLoads.add(pipeline.getLocation());
         }
      }

      if (!failedLoads.isEmpty()) {
         for (CompiledRenderPipeline builtPipeline : compilations.compiledOptionalPipelines) {
            if (builtPipeline != null) {
               builtPipeline.close();
            }
         }

         pipelineCache.close();
         throw new RuntimeException(
            "Failed to load required shader programs:\n" + failedLoads.stream().map(entry -> " - " + entry).collect(Collectors.joining("\n"))
         );
      } else {
         for (int ix = 0; ix < compilations.optionalPipelines.size(); ix++) {
            RenderPipeline pipeline = compilations.optionalPipelines.get(ix);
            CompiledRenderPipeline compiled = compilations.compiledOptionalPipelines.get(ix);
            if (compiled != null) {
               pipelineCache.insert(pipeline, compiled);
            } else {
               failedLoads.add(pipeline.getLocation());
            }
         }

         if (!failedLoads.isEmpty()) {
            LOGGER.warn("Failed to load optional shader programs:\n{}", failedLoads.stream().map(entry -> " - " + entry).collect(Collectors.joining("\n")));
         }

         this.postChains.close();
         this.postChains = newPostChains;
         PipelineCache oldPipelineCache = RenderSystem.setCurrentPipelineCache(pipelineCache);
         if (oldPipelineCache != null) {
            oldPipelineCache.close();
         }
      }
   }

   @Override
   public String getName() {
      return "Shader Loader";
   }

   private void tryTriggerRecovery(final Exception exception) {
      if (!this.postChains.triggeredRecovery) {
         this.recoveryHandler.accept(exception);
         this.postChains.triggeredRecovery = true;
      }
   }

   public boolean isPostEffectValid(final Identifier id, final Set<Identifier> allowedTargets) {
      PostChainConfig postChainConfig = this.postChains.configs.postChains.get(id);
      if (postChainConfig == null) {
         LOGGER.warn("Requested post effect does not exist: {}", id);
         return false;
      } else {
         Set<Identifier> invalidExternalTargets = Sets.difference(PostChain.getReferencedExternalTargets(postChainConfig), allowedTargets);
         if (!invalidExternalTargets.isEmpty()) {
            LOGGER.warn(
               "Requested post chain {} can not be used as a post effect because it uses targets inaccessible to post effects: {}", id, invalidExternalTargets
            );
            return false;
         } else {
            return true;
         }
      }
   }

   @Nullable
   public PostChain getPostChain(final Identifier id, final Set<Identifier> allowedTargets) {
      try {
         return this.postChains.getOrLoadPostChain(id, allowedTargets);
      } catch (ShaderManager$CompilationException var4) {
         LOGGER.error("Failed to load post chain: {}", id, var4);
         this.postChains.postChains.put(id, Optional.empty());
         this.tryTriggerRecovery(var4);
         return null;
      }
   }

   @Override
   public void close() {
      this.postChains.close();
      this.postChainProjectionMatrixBuffer.close();
   }

   public Stream<Identifier> getAvailablePostEffects() {
      return this.postChains.getKnownPostEffects();
   }
}
