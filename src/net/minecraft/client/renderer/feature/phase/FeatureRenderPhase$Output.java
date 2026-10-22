package net.minecraft.client.renderer.feature.phase;

import java.util.Collection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.feature.FeatureRendererType;
import net.minecraft.client.renderer.feature.submit.SubmitNode;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface FeatureRenderPhase$Output {
   void accept(SubmitNode submit, boolean strictlyOrdered);

   default <Submit extends SubmitNode> void acceptFeatureGroup(
      final FeatureRendererType<Submit> featureType, final Collection<Submit> submits, final boolean strictlyOrdered
   ) {
      for (Submit submit : submits) {
         if (submit.featureType() != featureType) {
            throw new IllegalArgumentException(submit + " was not of feature type " + featureType);
         }

         this.accept(submit, strictlyOrdered);
      }
   }
}
