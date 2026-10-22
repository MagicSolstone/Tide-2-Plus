package net.minecraft.client.renderer.feature.phase;

import com.google.common.primitives.Floats;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;

@Environment(EnvType.CLIENT)
public class TranslucentFeatureRenderPhase implements FeatureRenderPhase<TranslucentSubmit> {
   private final List<TranslucentSubmit> submits = new ArrayList<>();
   private final FloatList distances = new FloatArrayList();

   public void submit(final TranslucentSubmit submit) {
      this.submits.add(submit);
      this.distances.add(submit.distanceToCameraSq());
   }

   @Override
   public void sortInto(final FeatureRenderPhase$Output output) {
      if (!this.submits.isEmpty()) {
         for (int index : this.sortIndices()) {
            output.accept(this.submits.get(index), true);
         }

         this.submits.clear();
         this.distances.clear();
      }
   }

   private int[] sortIndices() {
      int[] indices = new int[this.submits.size()];
      int i = 0;

      while (i < this.submits.size()) {
         indices[i] = i++;
      }

      IntArrays.unstableSort(indices, (i1, i2) -> {
         int byDistance = Floats.compare(this.distances.getFloat(i2), this.distances.getFloat(i1));
         return byDistance != 0 ? byDistance : Integer.compare(i1, i2);
      });
      return indices;
   }

   @Override
   public boolean isEmpty() {
      return this.submits.isEmpty();
   }
}
