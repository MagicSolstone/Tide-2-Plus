package net.minecraft.world.item;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public interface ItemProvider {
   boolean hasNext();

   void restart();

   ItemStack next() throws NoSuchElementException;

   ItemStack peek() throws NoSuchElementException;

   boolean findNext(Predicate<ItemStack> predicate);

   default boolean findNextNonEmpty() {
      return this.findNext(s -> !s.isEmpty());
   }

   default ItemProvider orElseProvide(final ItemStack fallbackItem) {
      return new ItemProvider$FallbackItemProvider(this, fallbackItem);
   }

   static ItemProvider of(final ItemStack... items) {
      return new ArrayItemProvider(items);
   }

   static ItemProvider of(final Collection<ItemStack> items) {
      return of(items.toArray(new ItemStack[0]));
   }

   static ItemProvider cycle(final ItemStack... items) {
      return new ArrayItemProvider$Cycled(items);
   }

   static ItemProvider cycle(final Collection<ItemStack> items) {
      return cycle(items.toArray(new ItemStack[0]));
   }
}
