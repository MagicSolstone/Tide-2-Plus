package net.minecraft.world.level.storage.loot.providers.number.ints;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.Function;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public sealed interface ResolvableInt permits ResolvableInt$Constant, ResolvableInt$Reference {
   Codec<ResolvableInt> CODEC = Codec.either(ResolvableInt$Constant.CODEC, ResolvableInt$Reference.CODEC).xmap(Either::unwrap, ResolvableInt::wrap);
   StreamCodec<ByteBuf, ResolvableInt> STREAM_CODEC = ByteBufCodecs.either(ResolvableInt$Constant.STREAM_CODEC, ResolvableInt$Reference.STREAM_CODEC)
      .map(Either::unwrap, ResolvableInt::wrap);

   private static Either<ResolvableInt$Constant, ResolvableInt$Reference> wrap(final ResolvableInt resolvableNumber) {
      return switch (resolvableNumber) {
         case ResolvableInt$Constant constant -> Either.left(constant);
         case ResolvableInt$Reference reference -> Either.right(reference);
         default -> throw new MatchException(null, null);
      };
   }

   int get(LootContext context, int defaultValue);

   static ResolvableInt fromKey(final ResourceKey<ContextIntProvider> key) {
      return new ResolvableInt$Reference(key);
   }

   static <T> int getFromItem(
      final ItemStack itemStack,
      final DataComponentType<T> componentType,
      final Function<T, ResolvableInt> getter,
      final LootContext context,
      final int defaultValue
   ) {
      T component = itemStack.get(componentType);
      return component != null ? getter.apply(component).get(context, defaultValue) : defaultValue;
   }
}
