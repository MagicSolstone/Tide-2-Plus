package net.minecraft.client.renderer.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface Stitcher$SpriteLoader<T extends Stitcher$Entry> {
   void load(T entry, int x, int z, int padding);
}
