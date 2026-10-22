package net.minecraft.world.item.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record LevelBasedValue$Fraction(LevelBasedValue numerator, LevelBasedValue denominator) implements LevelBasedValue {
   public static final MapCodec<LevelBasedValue$Fraction> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            LevelBasedValue.CODEC.fieldOf("numerator").forGetter(LevelBasedValue$Fraction::numerator),
            LevelBasedValue.CODEC.fieldOf("denominator").forGetter(LevelBasedValue$Fraction::denominator)
         )
         .apply(i, LevelBasedValue$Fraction::new)
   );

   @Override
   public float calculate(final int level) {
      float denominator = this.denominator.calculate(level);
      return denominator == 0.0F ? 0.0F : this.numerator.calculate(level) / denominator;
   }

   @Override
   public MapCodec<LevelBasedValue$Fraction> codec() {
      return CODEC;
   }
}
