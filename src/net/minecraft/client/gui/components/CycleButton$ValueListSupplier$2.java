package net.minecraft.client.gui.components;

import java.util.List;
import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class CycleButton$ValueListSupplier$2 implements CycleButton$ValueListSupplier<T> {
   CycleButton$ValueListSupplier$2(final BooleanSupplier val$altSelector, final List val$altCopy, final List val$defaultCopy) {
      this.val$altSelector = val$altSelectorx;
      this.val$altCopy = val$altCopyx;
      this.val$defaultCopy = val$defaultCopyx;
   }

   @Override
   public List<T> getSelectedList() {
      return this.val$altSelector.getAsBoolean() ? this.val$altCopy : this.val$defaultCopy;
   }

   @Override
   public List<T> getDefaultList() {
      return this.val$defaultCopy;
   }
}
