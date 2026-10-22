package net.minecraft.world.item;

import java.util.function.Predicate;

public class ArrayItemProvider$Cycled extends ArrayItemProvider {
   public ArrayItemProvider$Cycled(final ItemStack[] items) {
      super(items);
   }

   @Override
   public boolean hasNext() {
      return this.items.length > 0;
   }

   @Override
   protected ItemStack computeNextItem() {
      return this.items[this.position % this.items.length].copy();
   }

   @Override
   public boolean findNext(final Predicate<ItemStack> predicate) {
      for (int offset = 0; offset < this.items.length; offset++) {
         int index = (this.position + offset) % this.items.length;
         if (predicate.test(this.items[index])) {
            this.setPosition(this.position + offset);
            return true;
         }
      }

      return false;
   }
}
