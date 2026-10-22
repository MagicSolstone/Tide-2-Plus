package net.minecraft.world.level.levelgen.densityfunction;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.Interval;

record DensityFunctionCompiler$PreparedCache(int id, Interval range, @DensityFunction$Axes int domainAxes, DensitySampler cachingSampler)
   implements DensityFunction {
   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      return this.cachingSampler;
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      return this;
   }

   @Override
   public MapCodec<DensityFunctionCompiler$PreparedCache> codec() {
      throw new UnsupportedOperationException("PreparedCache should never be encoded");
   }
}
