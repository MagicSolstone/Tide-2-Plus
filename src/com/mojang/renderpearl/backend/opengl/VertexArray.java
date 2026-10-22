package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.backend.api.BackendRenderPipeline$CreateInfo;
import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import java.util.Set;
import java.util.function.BiFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL33C;
import org.lwjgl.opengl.GLCapabilities;

@Environment(EnvType.CLIENT)
public abstract sealed class VertexArray implements UncheckedAutoCloseable permits VertexArray$Emulated, VertexArray$Separate {
   protected final int vertexArrayId = GL33C.glGenVertexArrays();

   public static BiFunction<GlProgram, BackendRenderPipeline$CreateInfo, VertexArray> createSource(
      final GLCapabilities capabilities, final Set<String> enabledExtensions
   ) {
      if (capabilities.GL_ARB_vertex_attrib_binding && GlDevice.USE_GL_ARB_vertex_attrib_binding) {
         enabledExtensions.add("GL_ARB_vertex_attrib_binding");
         return VertexArray$Separate::new;
      } else {
         return VertexArray$Emulated::new;
      }
   }

   private VertexArray() {
   }

   @Override
   public void close() {
      GL33C.glDeleteVertexArrays(this.vertexArrayId);
   }

   public abstract void bind(final GpuBufferSlice[] vertexBuffers);
}
