package net.minecraft.client.renderer.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record SpriteContents$FrameInfo(int index, int time) {
}
