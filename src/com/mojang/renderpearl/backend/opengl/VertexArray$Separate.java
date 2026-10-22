package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.backend.api.BackendRenderPipeline$CreateInfo;
import com.mojang.renderpearl.backend.api.BackendRenderPipeline$CreateInfo$AttribBinding;
import com.mojang.renderpearl.backend.api.BackendRenderPipeline$CreateInfo$VertexBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.ARBVertexAttribBinding;
import org.lwjgl.opengl.GL33C;

@Environment(EnvType.CLIENT)
final class VertexArray$Separate extends VertexArray {
   private final boolean needsMesaWorkaround;
   private final int[] strides = new int[16];

   private VertexArray$Separate(final GlProgram program, final BackendRenderPipeline$CreateInfo createInfo) {
      if ("Mesa".equals(GL33C.glGetString(7936))) {
         String version = GL33C.glGetString(7938);
         this.needsMesaWorkaround = version.contains("25.0.0") || version.contains("25.0.1") || version.contains("25.0.2");
      } else {
         this.needsMesaWorkaround = false;
      }

      GlStateManager._glBindVertexArray(this.vertexArrayId);

      for (BackendRenderPipeline$CreateInfo$VertexBuffer vertexBuffer : createInfo.vertexBuffers()) {
         this.strides[vertexBuffer.bufferSlot()] = vertexBuffer.stride();
         ARBVertexAttribBinding.glVertexBindingDivisor(vertexBuffer.bufferSlot(), vertexBuffer.stepRate());
      }

      for (BackendRenderPipeline$CreateInfo$AttribBinding attribBinding : createInfo.attribBindings()) {
         int attribLocation = attribBinding.location();
         GL33C.glEnableVertexAttribArray(attribLocation);
         int glExternalId = GlConst.toGlExternalId(attribBinding.format());
         int glType = GlConst.toGlType(attribBinding.format());
         boolean isIntegerFormat = GlConst.isGlFormatInteger(glExternalId);
         boolean isNormalizedFormat = GlConst.isFormatNormalized(attribBinding.format());
         int channelCount = GlConst.glFormatChannelCount(glExternalId);
         if (isIntegerFormat) {
            ARBVertexAttribBinding.glVertexAttribIFormat(attribLocation, channelCount, glType, attribBinding.offset());
         } else {
            ARBVertexAttribBinding.glVertexAttribFormat(attribLocation, channelCount, glType, isNormalizedFormat, attribBinding.offset());
         }

         ARBVertexAttribBinding.glVertexAttribBinding(attribLocation, attribBinding.bufferSlot());
      }

      GlStateManager._glBindVertexArray(0);
   }

   @Override
   public void bind(final GpuBufferSlice[] vertexBuffers) {
      GlStateManager._glBindVertexArray(this.vertexArrayId);

      for (int i = 0; i < vertexBuffers.length; i++) {
         if (this.strides[i] != 0) {
            GpuBufferSlice vertexBufferSlice = vertexBuffers[i];
            if (vertexBufferSlice == null) {
               throw new IllegalStateException("Vertex buffer slot " + i + " not specified but required");
            }

            GlBuffer vertexBuffer = (GlBuffer)vertexBufferSlice.buffer();
            if (this.needsMesaWorkaround) {
               ARBVertexAttribBinding.glBindVertexBuffer(i, 0, 0L, 0);
            }

            ARBVertexAttribBinding.glBindVertexBuffer(i, vertexBuffer.handle(), vertexBufferSlice.offset(), this.strides[i]);
         }
      }
   }
}
