package net.minecraft.world.level.levelgen.densityfunction;

import net.minecraft.core.Direction$Axis;
import net.minecraft.world.level.levelgen.densityfunction.generator.ConstantFunction;
import net.minecraft.world.level.levelgen.densityfunction.generator.GradientFunction;
import net.minecraft.world.level.levelgen.densityfunction.op.SliceFunction;

public record DfRewriteRule$SliceUniformAxes(@DensityFunction$Axes int parentDomainAxes) implements DfRewriteRule {
   @Override
   public DensityFunction rewrite(final DensityFunction function) {
      if (shouldSkip(function)) {
         return function;
      } else {
         int domainAxes = function.domainAxes();
         if (this.parentDomainAxes == domainAxes) {
            return function.rewriteChildren(this);
         } else {
            DensityFunction newFunction = function.rewriteChildren(new DfRewriteRule$SliceUniformAxes(domainAxes));
            int removedAxes = this.parentDomainAxes & ~domainAxes;
            return this.removeAxes(newFunction, removedAxes);
         }
      }
   }

   private DensityFunction removeAxes(DensityFunction function, @DensityFunction$Axes final int axes) {
      int filteredAxes = axes & ~getExistingRemovedAxes(function);
      if ((filteredAxes & 1) != 0) {
         function = new SliceFunction(Direction$Axis.X, 0, function);
      }

      if ((filteredAxes & 4) != 0) {
         function = new SliceFunction(Direction$Axis.Z, 0, function);
      }

      if ((filteredAxes & 2) != 0) {
         function = new SliceFunction(Direction$Axis.Y, 0, function);
      }

      return function;
   }

   @DensityFunction$Axes
   private static int getExistingRemovedAxes(DensityFunction function) {
      int axes = 0;

      while (function instanceof SliceFunction) {
         SliceFunction slice = (SliceFunction)function;
         axes |= DensityFunction.axesFrom(slice.axis());
         function = slice.input();
      }

      return axes;
   }

   private static boolean shouldSkip(final DensityFunction function) {
      return function instanceof ConstantFunction || function instanceof GradientFunction;
   }
}
