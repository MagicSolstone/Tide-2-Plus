package net.minecraft.client.gui.components;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class CycleButton$ValueListSupplier$1 implements CycleButton$ValueListSupplier<T> {
   CycleButton$ValueListSupplier$1(final List val$copy) {
      this.val$copy = val$copyx;
   }

   @Override
   public List<T> getSelectedList() {
      return this.val$copy;
   }

   @Override
   public List<T> getDefaultList() {
      return this.val$copy;
   }
}
