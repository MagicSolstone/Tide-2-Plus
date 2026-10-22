package net.minecraft.world.level.storage.loot;

import net.minecraft.util.context.ContextKey;
import org.jspecify.annotations.Nullable;

public interface LootContextArg$SimpleGetter<T> extends LootContextArg<T> {
   @Override
   ContextKey<? extends T> contextParam();

   @Nullable
   @Override
   default T get(final LootContext context) {
      return context.getOptional((ContextKey<T>)this.contextParam());
   }
}
