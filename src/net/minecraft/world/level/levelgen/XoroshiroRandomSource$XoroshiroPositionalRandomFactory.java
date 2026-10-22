package net.minecraft.world.level.levelgen;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class XoroshiroRandomSource$XoroshiroPositionalRandomFactory implements PositionalRandomFactory {
   private final long seedLo;
   private final long seedHi;

   public XoroshiroRandomSource$XoroshiroPositionalRandomFactory(final long seedLo, final long seedHi) {
      this.seedLo = seedLo;
      this.seedHi = seedHi;
   }

   @Override
   public RandomSource at(final int x, final int y, final int z) {
      long positionalSeed = Mth.getSeed(x, y, z);
      long randomSeed = positionalSeed ^ this.seedLo;
      return new XoroshiroRandomSource(randomSeed, this.seedHi);
   }

   @Override
   public RandomSource fromHashOf(final String name) {
      RandomSupport$Seed128bit seed = RandomSupport.seedFromHashOf(name);
      return new XoroshiroRandomSource(seed.xor(this.seedLo, this.seedHi));
   }

   @Override
   public RandomSource fromSeed(final long seed) {
      return new XoroshiroRandomSource(seed ^ this.seedLo, seed ^ this.seedHi);
   }

   @VisibleForTesting
   @Override
   public void parityConfigString(final StringBuilder sb) {
      sb.append("seedLo: ").append(this.seedLo).append(", seedHi: ").append(this.seedHi);
   }
}
