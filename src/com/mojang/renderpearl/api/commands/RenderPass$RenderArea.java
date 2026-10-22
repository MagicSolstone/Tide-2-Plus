package com.mojang.renderpearl.api.commands;

import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record RenderPass$RenderArea(int x, int y, int width, int height) {
   public boolean fillsTexture(final GpuTextureView texture) {
      return this.x == 0 && this.y == 0 && this.width == texture.getWidth(0) && this.height == texture.getHeight(0);
   }
}
