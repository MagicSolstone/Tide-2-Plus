package net.minecraft.world.entity;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.world.item.ItemStack;

class SlotAccess$1 implements SlotAccess {
   SlotAccess$1(final Supplier val$getter, final Consumer val$setter) {
      this.val$getter = val$getterx;
      this.val$setter = val$setterx;
   }

   @Override
   public ItemStack get() {
      return (ItemStack)this.val$getter.get();
   }

   @Override
   public boolean set(final ItemStack itemStack) {
      this.val$setter.accept(itemStack);
      return true;
   }
}
