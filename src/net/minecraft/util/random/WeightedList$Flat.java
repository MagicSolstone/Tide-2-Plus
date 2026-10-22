package net.minecraft.util.random;

import java.util.Arrays;
import java.util.List;

class WeightedList$Flat<E> implements WeightedList$Selector<E> {
   private final Object[] entries;

   private WeightedList$Flat(final List<Weighted<E>> entries, final int totalWeight) {
      this.entries = new Object[totalWeight];
      int i = 0;

      for (Weighted<E> entry : entries) {
         int weight = entry.weight();
         Arrays.fill(this.entries, i, i + weight, entry.value());
         i += weight;
      }
   }

   @Override
   public E get(final int selection) {
      return (E)this.entries[selection];
   }
}
