package net.minecraft.world.item.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;

public record LevelBasedValue$Lookup(List<Float> values, LevelBasedValue fallback) implements LevelBasedValue {
   public static final MapCodec<LevelBasedValue$Lookup> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Codec.FLOAT.listOf().fieldOf("values").forGetter(LevelBasedValue$Lookup::values),
            LevelBasedValue.CODEC.fieldOf("fallback").forGetter(LevelBasedValue$Lookup::fallback)
         )
         .apply(i, LevelBasedValue$Lookup::new)
   );

   @Override
   public float calculate(final int level) {
      return level <= this.values.size() ? this.values.get(level - 1) : this.fallback.calculate(level);
   }

   @Override
   public MapCodec<LevelBasedValue$Lookup> codec() {
      return CODEC;
   }
}
