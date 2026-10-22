package net.minecraft.world.level.storage.loot;

import net.minecraft.util.context.ContextKey;
import org.jspecify.annotations.Nullable;

public interface LootContextArg$Getter<T, R> extends LootContextArg<R> {
   @Nullable
   R get(T value);

   @Override
   ContextKey<? extends T> contextParam();

   @Nullable
   @Override
   default R get(final LootContext context) {
      T value = context.getOptional((ContextKey<T>)this.contextParam());
      return value != null ? this.get(value) : null;
   }
}
