package com.mojang.renderpearl.backend.opengl;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.logging.LogUtils;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout$UniformDescription;
import com.mojang.renderpearl.util.ShaderCompileException;
import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;
import org.lwjgl.opengl.GL33C;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class GlProgram implements UncheckedAutoCloseable {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final List<Uniform> uniforms = new ReferenceArrayList();
   @Nullable
   private Uniform$Ubo pushConstant = null;
   private final int programId;
   private final String debugLabel;

   private GlProgram(final int programId, final String debugLabel) {
      this.programId = programId;
      this.debugLabel = debugLabel;
   }

   public static GlProgram link(final List<GlShaderModule> compiledShaders, final String debugLabel) throws ShaderCompileException {
      int programId = GlStateManager.glCreateProgram();
      if (programId <= 0) {
         throw new ShaderCompileException("Could not create shader program (returned program ID " + programId + ")");
      } else {
         for (GlShaderModule shaderModule : compiledShaders) {
            GlStateManager.glAttachShader(programId, shaderModule.getShaderId());
         }

         GlStateManager.glLinkProgram(programId);
         int linkStatus = GlStateManager.glGetProgrami(programId, 35714);
         String linkMessage = GlStateManager.glGetProgramInfoLog(programId, 32768);
         if (linkStatus != 0 && !linkMessage.contains("Failed for unknown reason")) {
            if (!linkMessage.isEmpty()) {
               LOGGER.info("Info log when linking program containing {}. Log output: {}", shaderList(compiledShaders), linkMessage);
            }

            return new GlProgram(programId, debugLabel);
         } else {
            throw new ShaderCompileException(
               "Error encountered when linking program containing " + shaderList(compiledShaders) + ". Log output: " + linkMessage
            );
         }
      }
   }

   private static String shaderList(final List<GlShaderModule> compiledShaders) {
      return compiledShaders.stream().map(shader -> shader.getType() + " " + shader.getLabel()).collect(Collectors.joining(", "));
   }

   public void setupBindGroupLayouts(final List<BindGroupLayout$UniformDescription> uniforms) {
      int nextUboBinding = 0;
      int nextSamplerIndex = 0;
      GlStateManager._glUseProgram(this.programId);
      int pushConstantBlock = GL33C.glGetUniformBlockIndex(this.programId, "_push_constants");
      if (pushConstantBlock != -1) {
         int uboBinding = nextUboBinding++;
         GL33C.glUniformBlockBinding(this.programId, pushConstantBlock, uboBinding);
         this.pushConstant = new Uniform$Ubo(uboBinding);
      }

      for (int i = 0; i < uniforms.size(); i++) {
         BindGroupLayout$UniformDescription uniformDescription = uniforms.get(i);
         String uniformName = String.format(Locale.ROOT, "_uniform_%02d_%02d", 0, i);

         Uniform uniform = (Uniform)(switch (uniformDescription.type()) {
            case UNIFORM_BUFFER -> {
               int index = GL33C.glGetUniformBlockIndex(this.programId, uniformName);
               if (index == -1) {
                  yield null;
               } else {
                  int uboBinding = nextUboBinding++;
                  GL33C.glUniformBlockBinding(this.programId, index, uboBinding);
                  yield new Uniform$Ubo(uboBinding);
               }
            }
            case TEXEL_BUFFER -> {
               int location = GlStateManager._glGetUniformLocation(this.programId, uniformName);
               if (location == -1) {
                  yield null;
               } else {
                  int samplerIndex = nextSamplerIndex++;
                  GL33C.glUniform1i(location, samplerIndex);
                  yield new Uniform$Utb(samplerIndex, Objects.requireNonNull(uniformDescription.gpuFormat()));
               }
            }
            case COMBINED_IMAGE_SAMPLER -> {
               int location = GlStateManager._glGetUniformLocation(this.programId, uniformName);
               if (location == -1) {
                  yield null;
               } else {
                  int samplerIndex = nextSamplerIndex++;
                  GL33C.glUniform1i(location, samplerIndex);
                  yield new Uniform$Sampler(samplerIndex);
               }
            }
         });
         this.uniforms.add(uniform);
      }

      GlStateManager._glUseProgram(0);
   }

   @Override
   public void close() {
      this.uniforms.forEach(UncheckedAutoCloseable::safeClose);
      GlStateManager.glDeleteProgram(this.programId);
   }

   @Nullable
   public Uniform getUniform(final int index) {
      return index >= this.uniforms.size() ? null : this.uniforms.get(index);
   }

   public int uniformCount() {
      return this.uniforms.size();
   }

   @VisibleForTesting
   public int getProgramId() {
      return this.programId;
   }

   @Override
   public String toString() {
      return this.debugLabel;
   }

   public String getDebugLabel() {
      return this.debugLabel;
   }

   @Nullable
   public Uniform$Ubo pushConstant() {
      return this.pushConstant;
   }
}
