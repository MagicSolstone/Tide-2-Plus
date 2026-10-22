package net.minecraft.client.renderer.feature.submit;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.feature.FeatureRendererType;

@Environment(EnvType.CLIENT)
public interface SubmitNode {
   FeatureRendererType<? extends SubmitNode> featureType();
}
