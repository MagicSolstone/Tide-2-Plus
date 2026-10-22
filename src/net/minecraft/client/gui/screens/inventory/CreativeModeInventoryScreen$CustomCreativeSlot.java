package net.minecraft.client.gui.screens.inventory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
class CreativeModeInventoryScreen$CustomCreativeSlot extends Slot {
   public CreativeModeInventoryScreen$CustomCreativeSlot(final Container container, final int slot, final int x, final int y) {
      super(container, slot, x, y);
   }

   @Override
   public boolean mayPickup(final Player player) {
      ItemStack item = this.getItem();
      return super.mayPickup(player) && !item.isEmpty()
         ? item.isItemEnabled(player.level().enabledFeatures()) && !item.has(DataComponents.CREATIVE_SLOT_LOCK)
         : item.isEmpty();
   }
}
