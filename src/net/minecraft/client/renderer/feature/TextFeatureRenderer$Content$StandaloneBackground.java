package net.minecraft.client.renderer.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record TextFeatureRenderer$Content$StandaloneBackground(float x0, float y0, float x1, float y1, int color) implements TextFeatureRenderer$Content {
}
