package com.mojang.renderpearl.backend.common;

import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class BaseGpuTextureView implements GpuTextureView {
   private final GpuTexture texture;
   private final int baseMipLevel;
   private final int mipLevels;

   protected BaseGpuTextureView(final GpuTexture texture, final int baseMipLevel, final int mipLevels) {
      this.texture = texture;
      this.baseMipLevel = baseMipLevel;
      this.mipLevels = mipLevels;
   }

   @Override
   public GpuTexture texture() {
      return this.texture;
   }

   @Override
   public int baseMipLevel() {
      return this.baseMipLevel;
   }

   @Override
   public int mipLevels() {
      return this.mipLevels;
   }

   @Override
   public int getWidth(final int mipLevel) {
      return this.texture.getWidth(mipLevel + this.baseMipLevel);
   }

   @Override
   public int getHeight(final int mipLevel) {
      return this.texture.getHeight(mipLevel + this.baseMipLevel);
   }
}
