package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;

class HorseInventoryMenu$1 extends ArmorSlot {
   HorseInventoryMenu$1(
      final HorseInventoryMenu this$0,
      final Container inventory,
      final LivingEntity owner,
      final EquipmentSlot slot,
      final int slotIndex,
      final int x,
      final int y,
      final Identifier emptyIcon,
      final AbstractHorse val$horse
   ) {
      this.val$horse = val$horsex;
      Objects.requireNonNull(this$0);
      super(inventory, owner, slot, slotIndex, x, y, emptyIcon);
   }

   @Override
   public boolean isActive() {
      return this.val$horse.canUseSlot(EquipmentSlot.SADDLE) && this.val$horse.is(EntityTypeTags.CAN_EQUIP_SADDLE);
   }
}
