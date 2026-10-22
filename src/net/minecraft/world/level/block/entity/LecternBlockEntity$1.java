package net.minecraft.world.level.block.entity;

import java.util.Objects;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

class LecternBlockEntity$1 implements Container {
   LecternBlockEntity$1(final LecternBlockEntity this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public int getContainerSize() {
      return 1;
   }

   @Override
   public boolean isEmpty() {
      return this.this$0.book.isEmpty();
   }

   @Override
   public ItemStack getItem(final int slot) {
      return slot == 0 ? this.this$0.book : ItemStack.EMPTY;
   }

   @Override
   public ItemStack removeItem(final int slot, final int count) {
      if (slot == 0) {
         ItemStack result = this.this$0.book.split(count);
         if (this.this$0.book.isEmpty()) {
            this.this$0.onBookItemRemove();
         }

         return result;
      } else {
         return ItemStack.EMPTY;
      }
   }

   @Override
   public ItemStack removeItemNoUpdate(final int slot) {
      if (slot == 0) {
         ItemStack prev = this.this$0.book;
         this.this$0.book = ItemStack.EMPTY;
         this.this$0.onBookItemRemove();
         return prev;
      } else {
         return ItemStack.EMPTY;
      }
   }

   @Override
   public void setItem(final int slot, final ItemStack itemStack) {
   }

   @Override
   public int getMaxStackSize() {
      return 1;
   }

   @Override
   public void setChanged() {
      this.this$0.setChanged();
   }

   @Override
   public boolean stillValid(final Player player) {
      return Container.stillValidBlockEntity(this.this$0, player) && this.this$0.hasBook();
   }

   @Override
   public boolean canPlaceItem(final int slot, final ItemStack itemStack) {
      return false;
   }

   @Override
   public void clearContent() {
   }
}
