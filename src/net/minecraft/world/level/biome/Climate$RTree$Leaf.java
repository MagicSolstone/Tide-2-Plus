package net.minecraft.world.level.biome;

import org.jspecify.annotations.Nullable;

final class Climate$RTree$Leaf<T> extends Climate$RTree$Node<T> {
   private final T value;

   private Climate$RTree$Leaf(final Climate$ParameterPoint parameterPoint, final T value) {
      super(parameterPoint.parameterSpace());
      this.value = value;
   }

   @Override
   protected Climate$RTree$Leaf<T> search(final long[] target, @Nullable final Climate$RTree$Leaf<T> candidate, final Climate$DistanceMetric<T> distanceMetric) {
      return this;
   }
}
