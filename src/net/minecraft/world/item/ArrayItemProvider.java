package net.minecraft.world.item;

import java.util.NoSuchElementException;
import java.util.function.Predicate;
import org.jspecify.annotations.Nullable;

public class ArrayItemProvider implements ItemProvider {
   protected final ItemStack[] items;
   protected int position;
   @Nullable
   private ItemStack peekedItem;

   public ArrayItemProvider(final ItemStack[] items) {
      this.items = items;
   }

   @Override
   public boolean hasNext() {
      return this.position < this.items.length;
   }

   @Override
   public void restart() {
      this.setPosition(0);
   }

   @Override
   public final ItemStack next() {
      if (!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         ItemStack next = this.peekedItem != null ? this.peekedItem : this.computeNextItem();
         this.peekedItem = null;
         this.position++;
         return next;
      }
   }

   @Override
   public final ItemStack peek() {
      if (!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         if (this.peekedItem == null) {
            this.peekedItem = this.computeNextItem();
         }

         return this.peekedItem;
      }
   }

   protected ItemStack computeNextItem() {
      return this.items[this.position].copy();
   }

   @Override
   public boolean findNext(final Predicate<ItemStack> predicate) {
      for (int i = this.position; i < this.items.length; i++) {
         if (predicate.test(this.items[i])) {
            this.setPosition(i);
            return true;
         }
      }

      return false;
   }

   protected void setPosition(final int newPosition) {
      if (this.position != newPosition) {
         this.position = newPosition;
         this.peekedItem = null;
      }
   }
}
