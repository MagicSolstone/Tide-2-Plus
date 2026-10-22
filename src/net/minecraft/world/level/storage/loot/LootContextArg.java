package net.minecraft.world.level.storage.loot;

import com.mojang.serialization.Codec;
import java.util.function.UnaryOperator;
import net.minecraft.util.context.ContextKey;
import org.jspecify.annotations.Nullable;

public interface LootContextArg<R> {
   Codec<LootContextArg<Object>> ENTITY_OR_BLOCK = createArgCodec(
      builder -> builder.anyOf(LootContext$EntityTarget.values()).anyOf(LootContext$BlockEntityTarget.values())
   );

   @Nullable
   R get(LootContext context);

   ContextKey<?> contextParam();

   static <U> LootContextArg<U> cast(final LootContextArg<? extends U> original) {
      return (LootContextArg<U>)original;
   }

   static <R> LootContextArg<R> of(final ContextKey<? extends R> contextParam) {
      return () -> contextParam;
   }

   static <R> Codec<LootContextArg<R>> createArgCodec(final UnaryOperator<LootContextArg$ArgCodecBuilder<R>> consumer) {
      return consumer.apply(new LootContextArg$ArgCodecBuilder<>()).build();
   }
}
