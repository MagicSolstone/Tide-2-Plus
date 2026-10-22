package net.minecraft.world.level.biome;

import com.google.common.annotations.VisibleForTesting;
import java.util.Arrays;
import java.util.List;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;

@VisibleForTesting
abstract class Climate$RTree$Node<T> {
   protected final Climate$Parameter[] parameterSpace;

   protected Climate$RTree$Node(final List<Climate$Parameter> parameterSpace) {
      this.parameterSpace = parameterSpace.toArray(new Climate$Parameter[0]);
   }

   protected abstract Climate$RTree$Leaf<T> search(
      final long[] target, @Nullable final Climate$RTree$Leaf<T> candidate, final Climate$DistanceMetric<T> distanceMetric
   );

   protected long distance(final long[] target) {
      long distance = 0L;

      for (int i = 0; i < 7; i++) {
         distance += Mth.square(this.parameterSpace[i].distance(target[i]));
      }

      return distance;
   }

   @Override
   public String toString() {
      return Arrays.toString((Object[])this.parameterSpace);
   }
}
