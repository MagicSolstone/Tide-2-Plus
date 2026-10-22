package com.mojang.renderpearl.api.textures;

import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface GpuTextureView extends UncheckedAutoCloseable {
   boolean isClosed();

   GpuTexture texture();

   int baseMipLevel();

   int mipLevels();

   int getWidth(int mipLevel);

   int getHeight(int mipLevel);
}
