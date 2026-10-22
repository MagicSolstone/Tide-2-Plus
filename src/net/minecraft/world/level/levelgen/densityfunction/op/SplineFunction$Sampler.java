package net.minecraft.world.level.levelgen.densityfunction.op;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.BoundedFloatFunction;
import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

class SplineFunction$Sampler implements DensitySampler {
   private final BoundedFloatFunction<SplineFunction$SplineInput> sampler;
   private final int coordinateCount;

   public SplineFunction$Sampler(final DensityFunction$CompileContext context, final CubicSpline<SplineFunction$Coordinate> spline) {
      Map<DensityFunction, SplineFunction$SamplerCoordinate> coordinates = new HashMap<>();
      this.sampler = CubicSpline.asSampler(spline.mapCoordinates(coordinate -> coordinates.computeIfAbsent(coordinate.function(), function -> {
         int index = coordinates.size();
         DensitySampler sampler = function.compileSampler(context);
         return new SplineFunction$SamplerCoordinate(sampler, index, function.range());
      })));
      this.coordinateCount = coordinates.size();
   }

   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      try (SplineFunction$BufferSplineInput input = new SplineFunction$BufferSplineInput(context, volume, this.coordinateCount)) {
         for (int i = 0; i < outputBuffer.size(); i++) {
            input.index = i;
            outputBuffer.set(i, this.sampler.apply(input));
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return this.sampler.apply(new SplineFunction$PointSplineInput(context, blockX, blockY, blockZ, this.coordinateCount));
   }
}
