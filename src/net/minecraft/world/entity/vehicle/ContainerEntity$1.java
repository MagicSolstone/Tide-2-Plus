package net.minecraft.world.entity.vehicle;

import java.util.Objects;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;

class ContainerEntity$1 implements SlotAccess {
   ContainerEntity$1(final ContainerEntity this$0, final int val$slot) {
      this.val$slot = val$slotx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public ItemStack get() {
      return this.this$0.getChestVehicleItem(this.val$slot);
   }

   @Override
   public boolean set(final ItemStack itemStack) {
      this.this$0.setChestVehicleItem(this.val$slot, itemStack);
      return true;
   }
}
