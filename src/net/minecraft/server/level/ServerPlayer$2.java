package net.minecraft.server.level;

import java.util.Objects;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

class ServerPlayer$2 implements ContainerListener {
   ServerPlayer$2(final ServerPlayer this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void slotChanged(final AbstractContainerMenu container, final int slotIndex, final ItemStack changedItem) {
      Slot slot = container.getSlot(slotIndex);
      if (!(slot instanceof ResultSlot)) {
         if (slot.container == this.this$0.getInventory()) {
            CriteriaTriggers.INVENTORY_CHANGED.trigger(this.this$0, this.this$0.getInventory(), changedItem);
         }
      }
   }

   @Override
   public void dataChanged(final AbstractContainerMenu container, final int id, final int value) {
   }
}
