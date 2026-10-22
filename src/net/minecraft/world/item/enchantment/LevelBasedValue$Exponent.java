package net.minecraft.world.item.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record LevelBasedValue$Exponent(LevelBasedValue base, LevelBasedValue power) implements LevelBasedValue {
   public static final MapCodec<LevelBasedValue$Exponent> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            LevelBasedValue.CODEC.fieldOf("base").forGetter(LevelBasedValue$Exponent::base),
            LevelBasedValue.CODEC.fieldOf("power").forGetter(LevelBasedValue$Exponent::power)
         )
         .apply(i, LevelBasedValue$Exponent::new)
   );

   @Override
   public float calculate(final int level) {
      return (float)Math.pow(this.base.calculate(level), this.power.calculate(level));
   }

   @Override
   public MapCodec<LevelBasedValue$Exponent> codec() {
      return CODEC;
   }
}
