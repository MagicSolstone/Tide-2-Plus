package net.minecraft.client.renderer.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record Stitcher$Holder<T extends Stitcher$Entry>(T entry, int width, int height) {
}
