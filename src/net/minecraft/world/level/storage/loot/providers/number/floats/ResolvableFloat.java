package net.minecraft.world.level.storage.loot.providers.number.floats;

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

public sealed interface ResolvableFloat permits ResolvableFloat$Constant, ResolvableFloat$Reference {
   Codec<ResolvableFloat> CODEC = Codec.either(ResolvableFloat$Constant.CODEC, ResolvableFloat$Reference.CODEC).xmap(Either::unwrap, ResolvableFloat::wrap);
   StreamCodec<ByteBuf, ResolvableFloat> STREAM_CODEC = ByteBufCodecs.either(ResolvableFloat$Constant.STREAM_CODEC, ResolvableFloat$Reference.STREAM_CODEC)
      .map(Either::unwrap, ResolvableFloat::wrap);

   private static Either<ResolvableFloat$Constant, ResolvableFloat$Reference> wrap(final ResolvableFloat resolvableNumber) {
      return switch (resolvableNumber) {
         case ResolvableFloat$Constant constant -> Either.left(constant);
         case ResolvableFloat$Reference reference -> Either.right(reference);
         default -> throw new MatchException(null, null);
      };
   }

   float get(LootContext context, float defaultValue);

   static ResolvableFloat fromKey(final ResourceKey<ContextFloatProvider> key) {
      return new ResolvableFloat$Reference(key);
   }

   static <T> float getFromItem(
      final ItemStack itemStack,
      final DataComponentType<T> componentType,
      final Function<T, ResolvableFloat> getter,
      final LootContext context,
      final float defaultValue
   ) {
      T component = itemStack.get(componentType);
      return component != null ? getter.apply(component).get(context, defaultValue) : defaultValue;
   }
}
