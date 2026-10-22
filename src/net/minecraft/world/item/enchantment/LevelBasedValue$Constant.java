package net.minecraft.world.item.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record LevelBasedValue$Constant(float value) implements LevelBasedValue {
   public static final Codec<LevelBasedValue$Constant> CODEC = Codec.FLOAT.xmap(LevelBasedValue$Constant::new, LevelBasedValue$Constant::value);
   public static final MapCodec<LevelBasedValue$Constant> TYPED_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(Codec.FLOAT.fieldOf("value").forGetter(LevelBasedValue$Constant::value)).apply(i, LevelBasedValue$Constant::new)
   );

   @Override
   public float calculate(final int level) {
      return this.value;
   }

   @Override
   public MapCodec<LevelBasedValue$Constant> codec() {
      return TYPED_CODEC;
   }
}
