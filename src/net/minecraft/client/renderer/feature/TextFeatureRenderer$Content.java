package net.minecraft.client.renderer.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public sealed interface TextFeatureRenderer$Content permits TextFeatureRenderer$Content$Text, TextFeatureRenderer$Content$StandaloneBackground {
}
