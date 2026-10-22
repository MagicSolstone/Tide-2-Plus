package net.minecraft.world.level.levelgen.densityfunction;

import java.util.Objects;
import net.minecraft.world.level.levelgen.densityfunction.op.CacheFunction;

class DensityFunctionCompiler$1 implements DfRewriteRule {
   DensityFunctionCompiler$1(final DensityFunctionCompiler this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public DensityFunction rewrite(DensityFunction function) {
      function = DfRewriteRule.INLINE_REFERENCE.rewrite(function);
      return function instanceof CacheFunction cache ? this.this$0.reuseOrPrepareCache(cache) : function.rewriteChildren(this);
   }
}
