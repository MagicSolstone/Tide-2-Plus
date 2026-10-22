package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;
import org.lwjgl.opengl.ARBBufferStorage;
import org.lwjgl.opengl.GL33C;

@Environment(EnvType.CLIENT)
class DirectStateAccess$Emulated extends DirectStateAccess {
   private DirectStateAccess$Emulated() {
   }

   @Override
   public int createBuffer() {
      return GlStateManager._glGenBuffers();
   }

   @Override
   public void bufferData(final int buffer, final long size, @GpuBuffer$Usage final int usage) {
      int target = GlUtil.selectBufferBindTarget(usage);
      GlStateManager._glBindBuffer(target, buffer);
      GlStateManager._glBufferData(target, size, GlConst.bufferUsageToGlEnum(usage));
      GlStateManager._glBindBuffer(target, 0);
   }

   @Override
   public void bufferData(final int buffer, final ByteBuffer data, @GpuBuffer$Usage final int usage) {
      int target = GlUtil.selectBufferBindTarget(usage);
      GlStateManager._glBindBuffer(target, buffer);
      GlStateManager._glBufferData(target, data, GlConst.bufferUsageToGlEnum(usage));
      GlStateManager._glBindBuffer(target, 0);
   }

   @Override
   public void bufferSubData(final int buffer, final long offset, final ByteBuffer data, @GpuBuffer$Usage final int usage) {
      int target = GlUtil.selectBufferBindTarget(usage);
      GlStateManager._glBindBuffer(target, buffer);
      GlStateManager._glBufferSubData(target, offset, data);
      GlStateManager._glBindBuffer(target, 0);
   }

   @Override
   public void bufferStorage(final int buffer, final long size, @GpuBuffer$Usage final int usage) {
      int target = GlUtil.selectBufferBindTarget(usage);
      GlStateManager._glBindBuffer(target, buffer);
      ARBBufferStorage.glBufferStorage(target, size, GlConst.bufferUsageToGlFlag(usage));
      GlStateManager._glBindBuffer(target, 0);
   }

   @Override
   public void bufferStorage(final int buffer, final ByteBuffer data, @GpuBuffer$Usage final int usage) {
      int target = GlUtil.selectBufferBindTarget(usage);
      GlStateManager._glBindBuffer(target, buffer);
      ARBBufferStorage.glBufferStorage(target, data, GlConst.bufferUsageToGlFlag(usage));
      GlStateManager._glBindBuffer(target, 0);
   }

   @Nullable
   @Override
   public ByteBuffer mapBufferRange(final int buffer, final long offset, final long length, final int access, @GpuBuffer$Usage final int usage) {
      int target = GlUtil.selectBufferBindTarget(usage);
      GlStateManager._glBindBuffer(target, buffer);
      ByteBuffer byteBuffer = GlStateManager._glMapBufferRange(target, offset, length, access);
      GlStateManager._glBindBuffer(target, 0);
      return byteBuffer;
   }

   @Override
   public void unmapBuffer(final int buffer, @GpuBuffer$Usage final int usage) {
      int target = GlUtil.selectBufferBindTarget(usage);
      GlStateManager._glBindBuffer(target, buffer);
      GlStateManager._glUnmapBuffer(target);
      GlStateManager._glBindBuffer(target, 0);
   }

   @Override
   public void flushMappedBufferRange(final int buffer, final long offset, final long length, @GpuBuffer$Usage final int usage) {
      int target = GlUtil.selectBufferBindTarget(usage);
      GlStateManager._glBindBuffer(target, buffer);
      GL33C.glFlushMappedBufferRange(target, offset, length);
      GlStateManager._glBindBuffer(target, 0);
   }

   @Override
   public void copyBufferSubData(final int source, final int target, final long sourceOffset, final long targetOffset, final long length) {
      GlStateManager._glBindBuffer(36662, source);
      GlStateManager._glBindBuffer(36663, target);
      GL33C.glCopyBufferSubData(36662, 36663, sourceOffset, targetOffset, length);
      GlStateManager._glBindBuffer(36662, 0);
      GlStateManager._glBindBuffer(36663, 0);
   }

   @Override
   public int createFrameBufferObject() {
      return GlStateManager.glGenFramebuffers();
   }

   @Override
   public void bindFrameBufferTextures(
      final int fbo, final int[] color, final int[] colorMipLevels, final int depth, final int depthMipLevel, final int bindSlot
   ) {
      int tempBindSlot = bindSlot == 0 ? '販' : bindSlot;
      int oldFbo = GlStateManager.getFrameBuffer(tempBindSlot);
      GlStateManager._glBindFramebuffer(tempBindSlot, fbo);

      for (int i = 0; i < color.length; i++) {
         GlStateManager._glFramebufferTexture2D(tempBindSlot, 36064 + i, 3553, color[i], colorMipLevels[i]);
      }

      GlStateManager._glFramebufferTexture2D(tempBindSlot, 36096, 3553, depth, depthMipLevel);
      if (bindSlot == 0) {
         GlStateManager._glBindFramebuffer(tempBindSlot, oldFbo);
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
      int oldRead = GlStateManager.getFrameBuffer(36008);
      int oldDraw = GlStateManager.getFrameBuffer(36009);
      GlStateManager._glBindFramebuffer(36008, source);
      GlStateManager._glBindFramebuffer(36009, dest);
      GlStateManager._glBlitFrameBuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
      GlStateManager._glBindFramebuffer(36008, oldRead);
      GlStateManager._glBindFramebuffer(36009, oldDraw);
   }
}
