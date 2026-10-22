package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;
import org.lwjgl.opengl.ARBDirectStateAccess;

@Environment(EnvType.CLIENT)
class DirectStateAccess$Core extends DirectStateAccess {
   private DirectStateAccess$Core() {
   }

   @Override
   public int createBuffer() {
      GlStateManager.incrementTrackedBuffers();
      return ARBDirectStateAccess.glCreateBuffers();
   }

   @Override
   public void bufferData(final int buffer, final long size, @GpuBuffer$Usage final int usage) {
      ARBDirectStateAccess.glNamedBufferData(buffer, size, GlConst.bufferUsageToGlEnum(usage));
   }

   @Override
   public void bufferData(final int buffer, final ByteBuffer data, @GpuBuffer$Usage final int usage) {
      ARBDirectStateAccess.glNamedBufferData(buffer, data, GlConst.bufferUsageToGlEnum(usage));
   }

   @Override
   public void bufferSubData(final int buffer, final long offset, final ByteBuffer data, @GpuBuffer$Usage final int usage) {
      ARBDirectStateAccess.glNamedBufferSubData(buffer, offset, data);
   }

   @Override
   public void bufferStorage(final int buffer, final long size, @GpuBuffer$Usage final int usage) {
      ARBDirectStateAccess.glNamedBufferStorage(buffer, size, GlConst.bufferUsageToGlFlag(usage));
   }

   @Override
   public void bufferStorage(final int buffer, final ByteBuffer data, @GpuBuffer$Usage final int usage) {
      ARBDirectStateAccess.glNamedBufferStorage(buffer, data, GlConst.bufferUsageToGlFlag(usage));
   }

   @Nullable
   @Override
   public ByteBuffer mapBufferRange(final int buffer, final long offset, final long length, final int flags, @GpuBuffer$Usage final int usage) {
      return ARBDirectStateAccess.glMapNamedBufferRange(buffer, offset, length, flags);
   }

   @Override
   public void unmapBuffer(final int buffer, @GpuBuffer$Usage final int usage) {
      ARBDirectStateAccess.glUnmapNamedBuffer(buffer);
   }

   @Override
   public int createFrameBufferObject() {
      return ARBDirectStateAccess.glCreateFramebuffers();
   }

   @Override
   public void bindFrameBufferTextures(
      final int fbo, final int[] color, final int[] colorMipLevels, final int depth, final int depthMipLevel, final int bindSlot
   ) {
      for (int i = 0; i < color.length; i++) {
         ARBDirectStateAccess.glNamedFramebufferTexture(fbo, 36064 + i, color[i], colorMipLevels[i]);
      }

      ARBDirectStateAccess.glNamedFramebufferTexture(fbo, 36096, depth, depthMipLevel);
      if (bindSlot != 0) {
         GlStateManager._glBindFramebuffer(bindSlot, fbo);
      }
   }

   @Override
   public void blitFrameBuffers(
      final int source,
      final int dest,
      final int srcX0,
      final int srcY0,
      final int srcX1,
      final int srcY1,
      final int dstX0,
      final int dstY0,
      final int dstX1,
      final int dstY1,
      final int mask,
      final int filter
   ) {
      ARBDirectStateAccess.glBlitNamedFramebuffer(source, dest, srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
   }

   @Override
   public void flushMappedBufferRange(final int handle, final long offset, final long length, @GpuBuffer$Usage final int usage) {
      ARBDirectStateAccess.glFlushMappedNamedBufferRange(handle, offset, length);
   }

   @Override
   public void copyBufferSubData(final int source, final int target, final long sourceOffset, final long targetOffset, final long length) {
      ARBDirectStateAccess.glCopyNamedBufferSubData(source, target, sourceOffset, targetOffset, length);
   }
}
