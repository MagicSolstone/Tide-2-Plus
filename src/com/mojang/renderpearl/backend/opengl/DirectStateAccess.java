package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import java.nio.ByteBuffer;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;
import org.lwjgl.opengl.GLCapabilities;

@Environment(EnvType.CLIENT)
public abstract class DirectStateAccess {
   public static DirectStateAccess create(final GLCapabilities capabilities, final Set<String> enabledExtensions, final GlHeuristics heuristics) {
      if (capabilities.GL_ARB_direct_state_access && GlDevice.USE_GL_ARB_direct_state_access && !heuristics.isGlOnDx12()) {
         enabledExtensions.add("GL_ARB_direct_state_access");
         return new DirectStateAccess$Core();
      } else {
         return new DirectStateAccess$Emulated();
      }
   }

   public abstract int createBuffer();

   public abstract void bufferData(int buffer, long size, @GpuBuffer$Usage int usage);

   public abstract void bufferData(int buffer, ByteBuffer data, @GpuBuffer$Usage int usage);

   public abstract void bufferSubData(int buffer, long offset, ByteBuffer data, @GpuBuffer$Usage int usage);

   public abstract void bufferStorage(int buffer, long size, @GpuBuffer$Usage int usage);

   public abstract void bufferStorage(int buffer, ByteBuffer data, @GpuBuffer$Usage int usage);

   @Nullable
   public abstract ByteBuffer mapBufferRange(int buffer, long offset, long length, int access, @GpuBuffer$Usage int usage);

   public abstract void unmapBuffer(int buffer, @GpuBuffer$Usage int usage);

   public abstract int createFrameBufferObject();

   public abstract void bindFrameBufferTextures(int fbo, int[] color, int[] colorMipLevels, int depth, int depthMipLevel, int bindSlot);

   public void bindFrameBufferTextures(final int fbo, final int color, final int depth, final int mipLevel, final int bindSlot) {
      this.bindFrameBufferTextures(fbo, new int[]{color}, new int[]{mipLevel}, depth, mipLevel, bindSlot);
   }

   public abstract void blitFrameBuffers(
      int source, int dest, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter
   );

   public abstract void flushMappedBufferRange(final int handle, final long offset, final long length, @GpuBuffer$Usage final int usage);

   public abstract void copyBufferSubData(int source, int target, long sourceOffset, long targetOffset, long length);
}
