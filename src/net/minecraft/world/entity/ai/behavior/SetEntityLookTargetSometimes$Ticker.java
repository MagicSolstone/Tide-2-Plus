package net.minecraft.world.entity.ai.behavior;

import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;

public final class SetEntityLookTargetSometimes$Ticker {
   private final UniformInt interval;
   private int ticksUntilNextStart;

   public SetEntityLookTargetSometimes$Ticker(final UniformInt interval) {
      if (interval.minInclusive() <= 1) {
         throw new IllegalArgumentException();
      } else {
         this.interval = interval;
      }
   }

   public boolean tickDownAndCheck(final RandomSource random) {
      if (this.ticksUntilNextStart == 0) {
         this.ticksUntilNextStart = this.interval.sample(random) - 1;
         return false;
      } else {
         return --this.ticksUntilNextStart == 0;
      }
   }
}
