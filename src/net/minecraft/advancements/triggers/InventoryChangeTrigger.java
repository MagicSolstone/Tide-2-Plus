package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class InventoryChangeTrigger extends SimpleCriterionTrigger<InventoryChangeTrigger$TriggerInstance> {
   @Override
   public Codec<InventoryChangeTrigger$TriggerInstance> codec() {
      return InventoryChangeTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final Inventory inventory, final ItemStack changedItem) {
      int slotsFull = 0;
      int slotsEmpty = 0;
      int slotsOccupied = 0;

      for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
         ItemStack itemStack = inventory.getItem(slot);
         if (itemStack.isEmpty()) {
            slotsEmpty++;
         } else {
            slotsOccupied++;
            if (itemStack.getCount() >= itemStack.getMaxStackSize()) {
               slotsFull++;
            }
         }
      }

      this.trigger(player, inventory, changedItem, slotsFull, slotsEmpty, slotsOccupied);
   }

   private void trigger(
      final ServerPlayer player, final Inventory inventory, final ItemStack changedItem, final int slotsFull, final int slotsEmpty, final int slotsOccupied
   ) {
      this.trigger(player, t -> t.matches(inventory, changedItem, slotsFull, slotsEmpty, slotsOccupied));
   }
}
