package net.minecraft.world.item;

import java.util.function.Predicate;

public class ItemProvider$FallbackItemProvider implements ItemProvider {
   private final ItemProvider itemProvider;
   private final ItemStack fallbackItem;
   private boolean isUsingFallback;

   public ItemProvider$FallbackItemProvider(final ItemProvider itemProvider, final ItemStack fallbackItem) {
      this.itemProvider = itemProvider;
      this.fallbackItem = fallbackItem;
   }

   @Override
   public boolean hasNext() {
      return true;
   }

   @Override
   public void restart() {
      this.itemProvider.restart();
      this.isUsingFallback = false;
   }

   @Override
   public ItemProvider orElseProvide(final ItemStack fallbackItem) {
      return this;
   }

   @Override
   public ItemStack peek() {
      if (!this.isUsingFallback) {
         if (this.itemProvider.hasNext()) {
            return this.itemProvider.peek();
         }

         this.isUsingFallback = true;
      }

      return this.fallbackItem.copy();
   }

   @Override
   public ItemStack next() {
      if (!this.isUsingFallback) {
         if (this.itemProvider.hasNext()) {
            return this.itemProvider.next();
         }

         this.isUsingFallback = true;
      }

      return this.fallbackItem.copy();
   }

   @Override
   public boolean findNext(final Predicate<ItemStack> predicate) {
      if (this.isUsingFallback) {
         return predicate.test(this.fallbackItem);
      } else if (this.itemProvider.findNext(predicate)) {
         return true;
      } else if (predicate.test(this.fallbackItem)) {
         this.isUsingFallback = true;
         return true;
      } else {
         return false;
      }
   }
}
