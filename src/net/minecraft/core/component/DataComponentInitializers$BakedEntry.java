package net.minecraft.core.component;

import net.minecraft.core.Holder$Reference;

record DataComponentInitializers$BakedEntry<T>(Holder$Reference<T> element, DataComponentMap components) {
   public void apply() {
      this.element.bindComponents(this.components);
   }
}
