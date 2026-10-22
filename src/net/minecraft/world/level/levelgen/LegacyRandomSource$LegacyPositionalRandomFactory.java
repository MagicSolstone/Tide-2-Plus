package net.minecraft.world.level.levelgen;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class LegacyRandomSource$LegacyPositionalRandomFactory implements PositionalRandomFactory {
   private final long seed;

   public LegacyRandomSource$LegacyPositionalRandomFactory(final long seed) {
      this.seed = seed;
   }

   @Override
   public RandomSource at(final int x, final int y, final int z) {
      long positionalSeed = Mth.getSeed(x, y, z);
      long randomSeed = positionalSeed ^ this.seed;
      return new LegacyRandomSource(randomSeed);
   }

   @Override
   public RandomSource fromHashOf(final String name) {
      int positionalSeed = name.hashCode();
      return new LegacyRandomSource(positionalSeed ^ this.seed);
   }

   @Override
   public RandomSource fromSeed(final long seed) {
      return new LegacyRandomSource(seed);
   }

   @VisibleForTesting
   @Override
   public void parityConfigString(final StringBuilder sb) {
      sb.append("LegacyPositionalRandomFactory{").append(this.seed).append("}");
   }
}
