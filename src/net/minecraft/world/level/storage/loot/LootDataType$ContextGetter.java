package net.minecraft.world.level.storage.loot;

import net.minecraft.util.context.ContextKeySet;

@FunctionalInterface
public interface LootDataType$ContextGetter<T> {
   ContextKeySet context(T value);

   static <T> LootDataType$ContextGetter<T> constant(final ContextKeySet v) {
      return value -> v;
   }
}
