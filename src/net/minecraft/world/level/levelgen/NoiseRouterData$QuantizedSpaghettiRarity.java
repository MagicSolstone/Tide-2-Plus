package net.minecraft.world.level.levelgen;

import it.unimi.dsi.fastutil.floats.FloatList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public final class NoiseRouterData$QuantizedSpaghettiRarity {
   protected NoiseRouterData$QuantizedSpaghettiRarity() {
   }

   public static DensityFunction wrapRarity2d(final DensityFunction input, final Holder<NormalNoise> noise) {
      return DensityFunctions.intervalSelect(
            input,
            FloatList.of(new float[]{-0.75F, -0.5F, 0.5F, 0.75F}),
            List.of(
               noiseFunctionForRarity(noise, 0.5F),
               noiseFunctionForRarity(noise, 0.75F),
               noiseFunctionForRarity(noise, 1.0F),
               noiseFunctionForRarity(noise, 2.0F),
               noiseFunctionForRarity(noise, 3.0F)
            )
         )
         .abs();
   }

   public static DensityFunction wrapRarity3d(final DensityFunction input, final Holder<NormalNoise> noise) {
      return DensityFunctions.intervalSelect(
            input,
            FloatList.of(-0.5F, 0.0F, 0.5F),
            List.of(
               noiseFunctionForRarity(noise, 0.75F),
               noiseFunctionForRarity(noise, 1.0F),
               noiseFunctionForRarity(noise, 1.5F),
               noiseFunctionForRarity(noise, 2.0F)
            )
         )
         .abs();
   }

   private static DensityFunction noiseFunctionForRarity(final Holder<NormalNoise> noise, final float rarity) {
      return DensityFunctions.noise(noise, 1.0 / rarity, 1.0 / rarity).mul(rarity);
   }
}
