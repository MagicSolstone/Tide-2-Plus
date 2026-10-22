package net.minecraft.world.level.levelgen.densityfunction;

import net.minecraft.core.Holder;

@FunctionalInterface
public interface DfRewriteRule {
   DfRewriteRule INLINE_REFERENCE = function -> function instanceof DensityFunctions$HolderHolder(Holder patt1$temp)
      ? (DensityFunction)patt1$temp.value()
      : function;
   DfRewriteRule SLICE_UNIFORM_AXES = new DfRewriteRule$SliceUniformAxes(7);

   static DfRewriteRule sequence(final DfRewriteRule... rules) {
      return function -> {
         for (DfRewriteRule rule : rules) {
            function = rule.rewrite(function);
         }

         return function;
      };
   }

   DensityFunction rewrite(DensityFunction function);
}
