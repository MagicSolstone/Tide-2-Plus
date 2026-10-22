package net.minecraft.world.entity;

import java.util.Objects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.ticks.ContainerSingleItem;

class Mob$1 implements ContainerSingleItem {
   Mob$1(final Mob this$0, final EquipmentSlot val$slot) {
      this.val$slot = val$slotx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public ItemStack getTheItem() {
      return this.this$0.getItemBySlot(this.val$slot);
   }

   @Override
   public void setTheItem(final ItemStack itemStack) {
      this.this$0.setItemSlot(this.val$slot, itemStack);
      if (!itemStack.isEmpty()) {
         this.this$0.setGuaranteedDrop(this.val$slot);
         this.this$0.setPersistenceRequired();
      }
   }

   @Override
   public void setChanged() {
   }

   @Override
   public boolean stillValid(final Player player) {
      return player.getVehicle() == this.this$0 || player.isWithinEntityInteractionRange(this.this$0, 4.0);
   }
}
