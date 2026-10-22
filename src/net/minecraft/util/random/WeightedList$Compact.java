package net.minecraft.util.random;

import java.util.List;

class WeightedList$Compact<E> implements WeightedList$Selector<E> {
   private final Weighted<?>[] entries;

   private WeightedList$Compact(final List<Weighted<E>> entries) {
      this.entries = entries.toArray(Weighted[]::new);
   }

   @Override
   public E get(int selection) {
      for (Weighted<?> entry : this.entries) {
         selection -= entry.weight();
         if (selection < 0) {
            return (E)entry.value();
         }
      }

      throw new IllegalStateException(selection + " exceeded total weight");
   }
}
