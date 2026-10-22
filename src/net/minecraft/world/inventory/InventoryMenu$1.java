package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

class InventoryMenu$1 extends Slot {
   InventoryMenu$1(final InventoryMenu this$0, final Container container, final int slot, final int x, final int y, final Player val$owner) {
      this.val$owner = val$ownerx;
      Objects.requireNonNull(this$0);
      super(container, slot, x, y);
   }

   @Override
   public void setByPlayer(final ItemStack itemStack, final ItemStack previous) {
      this.val$owner.onEquipItem(EquipmentSlot.OFFHAND, previous, itemStack);
      super.setByPlayer(itemStack, previous);
   }

   @Override
   public Identifier getNoItemIcon() {
      return InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD;
   }
}
