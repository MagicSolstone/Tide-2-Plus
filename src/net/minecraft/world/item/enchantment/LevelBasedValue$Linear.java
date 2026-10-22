package net.minecraft.world.item.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record LevelBasedValue$Linear(float base, float perLevelAboveFirst) implements LevelBasedValue {
   public static final MapCodec<LevelBasedValue$Linear> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Codec.FLOAT.fieldOf("base").forGetter(LevelBasedValue$Linear::base),
            Codec.FLOAT.fieldOf("per_level_above_first").forGetter(LevelBasedValue$Linear::perLevelAboveFirst)
         )
         .apply(i, LevelBasedValue$Linear::new)
   );

   @Override
   public float calculate(final int level) {
      return this.base + this.perLevelAboveFirst * (level - 1);
   }

   @Override
   public MapCodec<LevelBasedValue$Linear> codec() {
      return CODEC;
   }
}
