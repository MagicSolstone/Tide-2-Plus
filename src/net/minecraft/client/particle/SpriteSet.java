package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public interface SpriteSet {
   TextureAtlasSprite get(final int index, final int max);

   TextureAtlasSprite get(RandomSource random);

   TextureAtlasSprite first();
}
