package net.minecraft.util.random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class WeightedList$Builder<E> {
   private final Builder<Weighted<E>> result = ImmutableList.builder();

   public WeightedList$Builder<E> add(final E item) {
      return this.add(item, 1);
   }

   public WeightedList$Builder<E> add(final E item, final int weight) {
      this.result.add(new Weighted<>(item, weight));
      return this;
   }

   public WeightedList$Builder<E> addAll(final WeightedList<E> other) {
      this.result.addAll(other.unwrap());
      return this;
   }

   public WeightedList<E> build() {
      return new WeightedList<>(this.result.build());
   }
}
