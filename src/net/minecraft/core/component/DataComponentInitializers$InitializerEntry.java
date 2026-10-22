package net.minecraft.core.component;

import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.resources.ResourceKey;

record DataComponentInitializers$InitializerEntry<T>(ResourceKey<T> key, DataComponentInitializers$Initializer<T> initializer) {
   public void run(final DataComponentMap$Builder components, final HolderLookup$Provider context) {
      this.initializer.run(components, context, this.key);
   }
}
