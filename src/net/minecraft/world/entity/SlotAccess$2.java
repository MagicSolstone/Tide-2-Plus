package net.minecraft.world.entity;

import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;

class SlotAccess$2 implements SlotAccess {
   SlotAccess$2(final LivingEntity val$entity, final EquipmentSlot val$slot, final Predicate val$validator) {
      this.val$entity = val$entityx;
      this.val$slot = val$slotx;
      this.val$validator = val$validatorx;
   }

   @Override
   public ItemStack get() {
      return this.val$entity.getItemBySlot(this.val$slot);
   }

   @Override
   public boolean set(final ItemStack itemStack) {
      if (!this.val$validator.test(itemStack)) {
         return false;
      } else {
         this.val$entity.setItemSlot(this.val$slot, itemStack);
         return true;
      }
   }
}
