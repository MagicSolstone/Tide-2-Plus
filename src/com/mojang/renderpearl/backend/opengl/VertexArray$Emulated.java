package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.backend.api.BackendRenderPipeline$CreateInfo;
import com.mojang.renderpearl.backend.api.BackendRenderPipeline$CreateInfo$AttribBinding;
import com.mojang.renderpearl.backend.api.BackendRenderPipeline$CreateInfo$VertexBuffer;
import java.util.Arrays;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL33C;

@Environment(EnvType.CLIENT)
final class VertexArray$Emulated extends VertexArray {
   private final int[] bufferStride = new int[16];
   private final int[] bufferDivisor = new int[16];
   private final int[] bufferIndex = new int[16];
   private final int[] channelCount = new int[16];
   private final int[] glType = new int[16];
   private final boolean[] isInteger = new boolean[16];
   private final boolean[] isNormalized = new boolean[16];
   private final int[] elementOffset = new int[16];

   private VertexArray$Emulated(final GlProgram program, final BackendRenderPipeline$CreateInfo createInfo) {
      GlStateManager._glBindVertexArray(this.vertexArrayId);

      for (BackendRenderPipeline$CreateInfo$VertexBuffer vertexBuffer : createInfo.vertexBuffers()) {
         this.bufferStride[vertexBuffer.bufferSlot()] = vertexBuffer.stride();
         this.bufferDivisor[vertexBuffer.bufferSlot()] = vertexBuffer.stepRate();
      }

      Arrays.fill(this.bufferIndex, -1);

      for (BackendRenderPipeline$CreateInfo$AttribBinding attribBinding : createInfo.attribBindings()) {
         int attribLocation = attribBinding.location();
         GL33C.glEnableVertexAttribArray(attribLocation);
         this.bufferIndex[attribLocation] = attribBinding.bufferSlot();
         int glExternalId = GlConst.toGlExternalId(attribBinding.format());
         this.glType[attribLocation] = GlConst.toGlType(attribBinding.format());
         this.isInteger[attribLocation] = GlConst.isGlFormatInteger(glExternalId);
         this.isNormalized[attribLocation] = GlConst.isFormatNormalized(attribBinding.format());
         this.channelCount[attribLocation] = GlConst.glFormatChannelCount(glExternalId);
         this.elementOffset[attribLocation] = attribBinding.offset();
      }

      GlStateManager._glBindVertexArray(0);
   }

   @Override
   public void bind(final GpuBufferSlice[] vertexBuffers) {
      GlStateManager._glBindVertexArray(this.vertexArrayId);

      for (int attributeIndex = 0; attributeIndex < 16; attributeIndex++) {
         if (this.bufferIndex[attributeIndex] != -1) {
            int vertexBufferIndex = this.bufferIndex[attributeIndex];
            if (vertexBuffers[vertexBufferIndex] == null) {
               throw new IllegalStateException("Vertex buffer slot " + vertexBufferIndex + " not specified but required");
            }

            GlStateManager._glBindBuffer(34962, ((GlBuffer)vertexBuffers[vertexBufferIndex].buffer()).handle());
            long totalOffset = vertexBuffers[vertexBufferIndex].offset() + this.elementOffset[attributeIndex];
            if (this.isInteger[attributeIndex]) {
               GL33C.glVertexAttribIPointer(
                  attributeIndex, this.channelCount[attributeIndex], this.glType[attributeIndex], this.bufferStride[vertexBufferIndex], totalOffset
               );
            } else {
               GL33C.glVertexAttribPointer(
                  attributeIndex,
                  this.channelCount[attributeIndex],
                  this.glType[attributeIndex],
                  this.isNormalized[attributeIndex],
                  this.bufferStride[vertexBufferIndex],
                  totalOffset
               );
            }

            GL33C.glVertexAttribDivisor(attributeIndex, this.bufferDivisor[vertexBufferIndex]);
         }
      }
   }
}
