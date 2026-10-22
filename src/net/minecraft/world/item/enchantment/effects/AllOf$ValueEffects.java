package net.minecraft.world.item.enchantment.effects;

import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.util.RandomSource;

public record AllOf$ValueEffects(List<EnchantmentValueEffect> effects) implements EnchantmentValueEffect {
   public static final MapCodec<AllOf$ValueEffects> CODEC = AllOf.codec(EnchantmentValueEffect.CODEC, AllOf$ValueEffects::new, AllOf$ValueEffects::effects);

   @Override
   public float process(final int enchantmentLevel, final RandomSource random, float value) {
      for (EnchantmentValueEffect effect : this.effects) {
         value = effect.process(enchantmentLevel, random, value);
      }

      return value;
   }

   @Override
   public MapCodec<AllOf$ValueEffects> codec() {
      return CODEC;
   }
}
