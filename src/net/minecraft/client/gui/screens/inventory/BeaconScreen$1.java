package net.minecraft.client.gui.screens.inventory;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
class BeaconScreen$1 implements ContainerListener {
   BeaconScreen$1(final BeaconScreen this$0, final BeaconMenu val$menu) {
      this.val$menu = val$menux;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void slotChanged(final AbstractContainerMenu container, final int slotIndex, final ItemStack itemStack) {
   }

   @Override
   public void dataChanged(final AbstractContainerMenu container, final int id, final int value) {
      this.this$0.primary = this.val$menu.getPrimaryEffect();
      this.this$0.secondary = this.val$menu.getSecondaryEffect();
   }
}
