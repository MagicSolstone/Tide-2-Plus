package net.minecraft.core.component;

import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

class DataComponentInitializers$1 implements DataComponentInitializers$PendingComponents<T> {
   DataComponentInitializers$1(final ResourceKey val$registryKey, final List val$entries) {
      this.val$registryKey = val$registryKeyx;
      this.val$entries = val$entriesx;
   }

   @Override
   public ResourceKey<? extends Registry<? extends T>> key() {
      return this.val$registryKey;
   }

   @Override
   public void forEach(final BiConsumer<Holder$Reference<T>, DataComponentMap> output) {
      this.val$entries.forEach(e -> output.accept(e.element, e.components));
   }

   @Override
   public void apply() {
      this.val$entries.forEach(DataComponentInitializers$BakedEntry::apply);
   }
}
