package com.mojang.renderpearl.backend.common;

import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.api.textures.GpuTexture$Usage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class BaseGpuTexture implements GpuTexture {
   private final GpuFormat format;
   private final int width;
   private final int height;
   private final int depthOrLayers;
   private final int mipLevels;
   @GpuTexture$Usage
   private final int usage;
   private final String label;

   public BaseGpuTexture(
      @GpuTexture$Usage final int usage,
      final String label,
      final GpuFormat format,
      final int width,
      final int height,
      final int depthOrLayers,
      final int mipLevels
   ) {
      this.usage = usage;
      this.label = label;
      this.format = format;
      this.width = width;
      this.height = height;
      this.depthOrLayers = depthOrLayers;
      this.mipLevels = mipLevels;
   }

   @Override
   public int getWidth(final int mipLevel) {
      return this.width >> mipLevel;
   }

   @Override
   public int getHeight(final int mipLevel) {
      return this.height >> mipLevel;
   }

   @Override
   public int getDepthOrLayers() {
      return this.depthOrLayers;
   }

   @Override
   public int getMipLevels() {
      return this.mipLevels;
   }

   @Override
   public GpuFormat getFormat() {
      return this.format;
   }

   @GpuTexture$Usage
   @Override
   public int usage() {
      return this.usage;
   }

   @Override
   public String getLabel() {
      return this.label;
   }
}
