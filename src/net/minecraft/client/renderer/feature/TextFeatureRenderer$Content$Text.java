package net.minecraft.client.renderer.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.FormattedCharSequence;

@Environment(EnvType.CLIENT)
public record TextFeatureRenderer$Content$Text(
   float x, float y, FormattedCharSequence string, boolean dropShadow, int color, int backgroundColor, int outlineColor
) implements TextFeatureRenderer$Content {
}
