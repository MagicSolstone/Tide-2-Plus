package net.minecraft.client.renderer.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record GameTestBlockHighlightRenderer$Marker(int color, String text, long removeAtTime) {
}
