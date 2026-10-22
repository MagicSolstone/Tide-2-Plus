package net.minecraft.world.level.levelgen;

import java.util.Objects;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler$Bound;
import net.minecraft.world.level.levelgen.densityfunction.DensitySamplerSet;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

class RandomState$2 implements DensitySamplerSet {
   RandomState$2(final RandomState this$0, final SamplerContext val$context) {
      this.val$context = val$contextx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public DensitySampler$Bound get(final DensityFunction function) {
      return this.this$0.getSampler(function).bind(this.val$context);
   }

   @Override
   public float sampleValue(final DensityFunction function, final int blockX, final int blockY, final int blockZ) {
      return this.this$0.getSampler(function).sampleValue(this.val$context, blockX, blockY, blockZ);
   }
}
