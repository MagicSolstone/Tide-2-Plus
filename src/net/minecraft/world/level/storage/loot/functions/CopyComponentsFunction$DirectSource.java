package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootContextArg$Getter;

record CopyComponentsFunction$DirectSource<T extends DataComponentGetter>(ContextKey<? extends T> contextParam)
   implements LootContextArg$Getter<T, DataComponentGetter> {
   public DataComponentGetter get(final T value) {
      return value;
   }
}
