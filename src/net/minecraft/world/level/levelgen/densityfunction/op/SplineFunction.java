package net.minecraft.world.level.levelgen.densityfunction.op;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.CubicSpline;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

public record SplineFunction(CubicSpline<SplineFunction$Coordinate> spline) implements DensityFunction {
   private static final Codec<CubicSpline<SplineFunction$Coordinate>> SPLINE_CODEC = CubicSpline.codec(SplineFunction$Coordinate.CODEC);
   public static final MapCodec<SplineFunction> CODEC = SPLINE_CODEC.fieldOf("spline").xmap(SplineFunction::new, SplineFunction::spline);

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      return new SplineFunction$Sampler(context, this.spline);
   }

   @Override
   public Interval range() {
      return this.spline.range();
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      MutableInt axes = new MutableInt(0);
      this.spline.forEachCoordinate(coordinate -> axes.setValue(axes.intValue() | coordinate.function().domainAxes()));
      return axes.intValue();
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      MutableBoolean changed = new MutableBoolean();
      CubicSpline<SplineFunction$Coordinate> newSpline = this.spline.mapCoordinates(coordinate -> {
         DensityFunction function = coordinate.function();
         DensityFunction newFunction = rule.rewrite(function);
         if (newFunction != function) {
            changed.setTrue();
            return new SplineFunction$Coordinate(newFunction);
         } else {
            return coordinate;
         }
      });
      return changed.isFalse() ? this : new SplineFunction(newSpline);
   }

   @Override
   public MapCodec<SplineFunction> codec() {
      return CODEC;
   }

   @Override
   public String toString() {
      return this.spline.toString();
   }
}
