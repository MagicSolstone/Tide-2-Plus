package net.minecraft.world.entity;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.world.item.ItemStack;

public interface SlotAccess {
   ItemStack get();

   boolean set(ItemStack itemStack);

   static SlotAccess of(final Supplier<ItemStack> getter, final Consumer<ItemStack> setter) {
      return new SlotAccess$1(getter, setter);
   }

   static SlotAccess forEquipmentSlot(final LivingEntity entity, final EquipmentSlot slot, final Predicate<ItemStack> validator) {
      return new SlotAccess$2(entity, slot, validator);
   }

   static SlotAccess forEquipmentSlot(final LivingEntity entity, final EquipmentSlot slot) {
      return forEquipmentSlot(entity, slot, stack -> true);
   }

   static SlotAccess forListElement(final List<ItemStack> stacks, final int index) {
      return new SlotAccess$3(stacks, index);
   }
}
