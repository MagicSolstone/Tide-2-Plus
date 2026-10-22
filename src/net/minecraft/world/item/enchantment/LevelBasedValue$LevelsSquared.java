package net.minecraft.world.item.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

public record LevelBasedValue$LevelsSquared(float added) implements LevelBasedValue {
   public static final MapCodec<LevelBasedValue$LevelsSquared> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(Codec.FLOAT.fieldOf("added").forGetter(LevelBasedValue$LevelsSquared::added)).apply(i, LevelBasedValue$LevelsSquared::new)
   );

   @Override
   public float calculate(final int level) {
      return Mth.square(level) + this.added;
   }

   @Override
   public MapCodec<LevelBasedValue$LevelsSquared> codec() {
      return CODEC;
   }
}
