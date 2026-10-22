package net.minecraft.client.renderer.feature.phase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.renderer.feature.FeatureRendererType;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.ArrayUtils;

@Environment(EnvType.CLIENT)
public class SimpleFeatureRenderPhase implements FeatureRenderPhase<SubmitNode> {
   private SimpleFeatureRenderPhase$FeatureSubmits<?>[] submitsByFeature = new SimpleFeatureRenderPhase$FeatureSubmits[0];

   @Override
   public void submit(final SubmitNode submit) {
      FeatureRendererType<? extends SubmitNode> type = submit.featureType();
      if (this.submitsByFeature.length <= type.id()) {
         this.submitsByFeature = Arrays.copyOf(this.submitsByFeature, Mth.roundToward(type.id() + 1, 16));
      }

      SimpleFeatureRenderPhase$FeatureSubmits<?> submits = this.submitsByFeature[type.id()];
      if (submits == null) {
         submits = new SimpleFeatureRenderPhase$FeatureSubmits<>(type);
         this.submitsByFeature[type.id()] = submits;
      }

      submits.addUnchecked(submit);
   }

   @Override
   public void sortInto(final FeatureRenderPhase$Output output) {
      for (SimpleFeatureRenderPhase$FeatureSubmits<?> submits : maybeShuffle(this.submitsByFeature)) {
         if (submits != null) {
            sortFeatureInto(output, submits);
         }
      }

      this.clear();
   }

   private static <Submit extends SubmitNode> void sortFeatureInto(
      final FeatureRenderPhase$Output output, final SimpleFeatureRenderPhase$FeatureSubmits<Submit> submits
   ) {
      output.acceptFeatureGroup(submits.featureType, maybeShuffle(submits.unbatched), false);

      for (List<Submit> batch : maybeShuffle(submits.batches.values())) {
         output.acceptFeatureGroup(submits.featureType, maybeShuffle(batch), false);
      }
   }

   private static <V> Collection<V> maybeShuffle(final Collection<V> collection) {
      if (SharedConstants.DEBUG_SHUFFLE_MODELS) {
         List<V> shuffled = new ArrayList<>(collection);
         Collections.shuffle(shuffled);
         return shuffled;
      } else {
         return collection;
      }
   }

   private static <V> V[] maybeShuffle(final V[] array) {
      if (SharedConstants.DEBUG_SHUFFLE_MODELS) {
         V[] shuffled = Arrays.copyOf(array, array.length);
         ArrayUtils.shuffle(shuffled);
         return shuffled;
      } else {
         return array;
      }
   }

   public void clear() {
      for (int i = 0; i < this.submitsByFeature.length; i++) {
         SimpleFeatureRenderPhase$FeatureSubmits<?> submits = this.submitsByFeature[i];
         if (submits != null) {
            if (submits.isEmpty()) {
               this.submitsByFeature[i] = null;
            } else {
               submits.clear();
            }
         }
      }
   }

   @Override
   public boolean isEmpty() {
      for (SimpleFeatureRenderPhase$FeatureSubmits<?> submits : this.submitsByFeature) {
         if (submits != null && !submits.isEmpty()) {
            return false;
         }
      }

      return true;
   }
}
