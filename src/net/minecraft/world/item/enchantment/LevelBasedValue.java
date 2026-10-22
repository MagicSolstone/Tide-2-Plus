package net.minecraft.world.item.enchantment;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public interface LevelBasedValue {
   Codec<LevelBasedValue> DISPATCH_CODEC = BuiltInRegistries.ENCHANTMENT_LEVEL_BASED_VALUE_TYPE.byNameCodec().dispatch(LevelBasedValue::codec, c -> c);
   Codec<LevelBasedValue> CODEC = Codec.either(LevelBasedValue$Constant.CODEC, DISPATCH_CODEC)
      .xmap(
         either -> (LevelBasedValue)either.map(l -> l, r -> r),
         levelBasedValue -> levelBasedValue instanceof LevelBasedValue$Constant constant ? Either.left(constant) : Either.right(levelBasedValue)
      );

   static MapCodec<? extends LevelBasedValue> bootstrap(final Registry<MapCodec<? extends LevelBasedValue>> registry) {
      Registry.register(registry, "clamped", LevelBasedValue$Clamped.CODEC);
      Registry.register(registry, "fraction", LevelBasedValue$Fraction.CODEC);
      Registry.register(registry, "levels_squared", LevelBasedValue$LevelsSquared.CODEC);
      Registry.register(registry, "linear", LevelBasedValue$Linear.CODEC);
      Registry.register(registry, "exponent", LevelBasedValue$Exponent.CODEC);
      return Registry.register(registry, "lookup", LevelBasedValue$Lookup.CODEC);
   }

   static LevelBasedValue$Constant constant(final float value) {
      return new LevelBasedValue$Constant(value);
   }

   static LevelBasedValue$Linear perLevel(final float base, final float perLevelAboveFirst) {
      return new LevelBasedValue$Linear(base, perLevelAboveFirst);
   }

   static LevelBasedValue$Linear perLevel(final float perLevel) {
      return perLevel(perLevel, perLevel);
   }

   static LevelBasedValue$Lookup lookup(final List<Float> values, final LevelBasedValue fallback) {
      return new LevelBasedValue$Lookup(values, fallback);
   }

   float calculate(int level);

   MapCodec<? extends LevelBasedValue> codec();
}
