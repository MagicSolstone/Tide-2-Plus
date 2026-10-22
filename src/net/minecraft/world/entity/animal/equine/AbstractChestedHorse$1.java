package net.minecraft.world.entity.animal.equine;

import java.util.Objects;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

class AbstractChestedHorse$1 implements SlotAccess {
   AbstractChestedHorse$1(final AbstractChestedHorse this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public ItemStack get() {
      return this.this$0.hasChest() ? new ItemStack(Items.CHEST) : ItemStack.EMPTY;
   }

   @Override
   public boolean set(final ItemStack itemStack) {
      if (itemStack.isEmpty()) {
         if (this.this$0.hasChest()) {
            this.this$0.setChest(false);
            this.this$0.createInventory();
         }

         return true;
      } else if (itemStack.is(Items.CHEST)) {
         if (!this.this$0.hasChest()) {
            this.this$0.setChest(true);
            this.this$0.createInventory();
         }

         return true;
      } else {
         return false;
      }
   }
}
