package net.minecraft.world.item.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

public record LevelBasedValue$Clamped(LevelBasedValue value, float min, float max) implements LevelBasedValue {
   public static final MapCodec<LevelBasedValue$Clamped> CODEC = RecordCodecBuilder.mapCodec(
         i -> i.group(
               LevelBasedValue.CODEC.fieldOf("value").forGetter(LevelBasedValue$Clamped::value),
               Codec.FLOAT.fieldOf("min").forGetter(LevelBasedValue$Clamped::min),
               Codec.FLOAT.fieldOf("max").forGetter(LevelBasedValue$Clamped::max)
            )
            .apply(i, LevelBasedValue$Clamped::new)
      )
      .validate(u -> u.max <= u.min ? DataResult.error(() -> "Max must be larger than min, min: " + u.min + ", max: " + u.max) : DataResult.success(u));

   @Override
   public float calculate(final int level) {
      return Mth.clamp(this.value.calculate(level), this.min, this.max);
   }

   @Override
   public MapCodec<LevelBasedValue$Clamped> codec() {
      return CODEC;
   }
}
