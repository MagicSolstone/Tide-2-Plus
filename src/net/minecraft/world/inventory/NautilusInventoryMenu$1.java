package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;

class NautilusInventoryMenu$1 extends ArmorSlot {
   NautilusInventoryMenu$1(
      final NautilusInventoryMenu this$0,
      final Container inventory,
      final LivingEntity owner,
      final EquipmentSlot slot,
      final int slotIndex,
      final int x,
      final int y,
      final Identifier emptyIcon,
      final AbstractNautilus val$nautilus
   ) {
      this.val$nautilus = val$nautilusx;
      Objects.requireNonNull(this$0);
      super(inventory, owner, slot, slotIndex, x, y, emptyIcon);
   }

   @Override
   public boolean isActive() {
      return this.val$nautilus.canUseSlot(EquipmentSlot.SADDLE);
   }
}
