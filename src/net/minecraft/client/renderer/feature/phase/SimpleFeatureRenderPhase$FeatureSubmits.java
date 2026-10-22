package net.minecraft.client.renderer.feature.phase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.feature.FeatureRendererType;
import net.minecraft.client.renderer.feature.submit.BatchableSubmit;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class SimpleFeatureRenderPhase$FeatureSubmits<Submit extends SubmitNode> {
   private final FeatureRendererType<Submit> featureType;
   private final List<Submit> unbatched = new ArrayList<>();
   private final Map<Object, List<Submit>> batches = new HashMap<>();

   private SimpleFeatureRenderPhase$FeatureSubmits(final FeatureRendererType<Submit> featureType) {
      this.featureType = featureType;
   }

   public void addUnchecked(final SubmitNode submit) {
      this.add((Submit)submit);
   }

   public void add(final Submit submit) {
      Object key = batchKey(submit);
      if (key == null) {
         this.unbatched.add(submit);
      } else {
         this.batches.computeIfAbsent(key, var0 -> new ArrayList<>()).add(submit);
      }
   }

   @Nullable
   private static Object batchKey(final SubmitNode submit) {
      return submit instanceof BatchableSubmit batchable ? batchable.batchKey() : null;
   }

   public boolean isEmpty() {
      if (!this.unbatched.isEmpty()) {
         return false;
      } else {
         for (List<Submit> submits : this.batches.values()) {
            if (!submits.isEmpty()) {
               return false;
            }
         }

         return true;
      }
   }

   public void clear() {
      this.unbatched.clear();
      this.batches.values().removeIf(submits -> {
         if (submits.isEmpty()) {
            return true;
         } else {
            submits.clear();
            return false;
         }
      });
   }
}
