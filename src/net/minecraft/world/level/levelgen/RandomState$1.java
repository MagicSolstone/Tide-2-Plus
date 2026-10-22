package net.minecraft.world.level.levelgen;

import java.util.Objects;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.Noise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

class RandomState$1 implements DensityFunction$CompileContext {
   RandomState$1(final RandomState this$0, final long val$seed, final boolean val$useLegacyRandom) {
      this.val$seed = val$seedx;
      this.val$useLegacyRandom = val$useLegacyRandomx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   private RandomSource newLegacyInstance(final long seedOffset) {
      return new LegacyRandomSource(this.val$seed + seedOffset);
   }

   @Override
   public Noise createNoiseSampler(final Holder<NormalNoise> parameters) {
      if (parameters.is(Noises.TEMPERATURE_NETHER)) {
         return parameters.value().createForLegacyNetherBiome(this.newLegacyInstance(0L));
      } else {
         return parameters.is(Noises.VEGETATION_NETHER)
            ? parameters.value().createForLegacyNetherBiome(this.newLegacyInstance(1L))
            : this.this$0.getOrCreateNoise(parameters.unwrapKey().orElseThrow());
      }
   }

   @Override
   public RandomSource createRandom(final Identifier seed) {
      return this.val$useLegacyRandom && seed.equals(BlendedNoise.NOISE_SEED) ? this.newLegacyInstance(0L) : this.this$0.random.fromHashOf(seed);
   }

   @Override
   public RandomSource createEndIslandRandom() {
      return new LegacyRandomSource(this.val$seed);
   }
}
