package net.minecraft.world.item.enchantment.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.Function;

public interface AllOf {
   static <T, A extends T> MapCodec<A> codec(final Codec<T> topLevelCodec, final Function<List<T>, A> constructor, final Function<A, List<T>> accessor) {
      return RecordCodecBuilder.mapCodec(i -> i.group(topLevelCodec.listOf().fieldOf("effects").forGetter(accessor)).apply(i, constructor));
   }

   static AllOf$EntityEffects entityEffects(final EnchantmentEntityEffect... effects) {
      return new AllOf$EntityEffects(List.of(effects));
   }

   static AllOf$LocationBasedEffects locationBasedEffects(final EnchantmentLocationBasedEffect... effects) {
      return new AllOf$LocationBasedEffects(List.of(effects));
   }

   static AllOf$ValueEffects valueEffects(final EnchantmentValueEffect... effects) {
      return new AllOf$ValueEffects(List.of(effects));
   }
}
