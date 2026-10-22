package net.minecraft.client.gui.screens.inventory;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
class LecternScreen$1 implements ContainerListener {
   LecternScreen$1(final LecternScreen this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void slotChanged(final AbstractContainerMenu container, final int slotIndex, final ItemStack itemStack) {
      this.this$0.bookChanged();
   }

   @Override
   public void dataChanged(final AbstractContainerMenu container, final int id, final int value) {
      if (id == 0) {
         this.this$0.pageChanged();
      }
   }
}
