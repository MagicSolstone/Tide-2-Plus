package net.minecraft.client.renderer.feature.phase;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.feature.submit.SubmitNode;

@Environment(EnvType.CLIENT)
public interface FeatureRenderPhase<Submit extends SubmitNode> {
   void submit(Submit submit);

   void sortInto(FeatureRenderPhase$Output output);

   boolean isEmpty();
}
