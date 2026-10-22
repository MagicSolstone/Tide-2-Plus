package net.minecraft.world.level.levelgen;

import java.util.function.LongFunction;
import net.minecraft.util.RandomSource;

public enum WorldgenRandom$Algorithm {
   LEGACY(LegacyRandomSource::new),
   XOROSHIRO(XoroshiroRandomSource::new);

   private final LongFunction<RandomSource> constructor;

   private WorldgenRandom$Algorithm(final LongFunction<RandomSource> constructor) {
      this.constructor = constructor;
   }

   public RandomSource newInstance(final long seed) {
      return this.constructor.apply(seed);
   }
}
