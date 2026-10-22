package com.mojang.renderpearl.api.device;

import com.mojang.renderpearl.api.GpuFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record DeviceLimits(
   int maxAnisotropy,
   int minUniformOffsetAlignment,
   int maxTextureSize,
   long maxMemoryAllocationSize,
   int maxMultiDrawDirectInterleavedDrawCount,
   int maxColorAttachments,
   int maxDrawIndirectDrawCount
) {
   public int maxTextureSizeForFormat(final GpuFormat format) {
      return Integer.highestOneBit(Math.min(this.maxTextureSize, (int)Math.sqrt((double)this.maxMemoryAllocationSize / format.blockSize())));
   }
}
