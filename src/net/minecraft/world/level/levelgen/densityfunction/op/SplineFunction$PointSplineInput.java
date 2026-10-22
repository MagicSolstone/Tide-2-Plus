package net.minecraft.world.level.levelgen.densityfunction.op;

import java.util.Arrays;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

class SplineFunction$PointSplineInput implements SplineFunction$SplineInput {
   private final SamplerContext context;
   private final int blockX;
   private final int blockY;
   private final int blockZ;
   private final float[] cachedValues;

   private SplineFunction$PointSplineInput(final SamplerContext context, final int blockX, final int blockY, final int blockZ, final int coordinateCount) {
      this.context = context;
      this.blockX = blockX;
      this.blockY = blockY;
      this.blockZ = blockZ;
      this.cachedValues = new float[coordinateCount];
      Arrays.fill(this.cachedValues, Float.NaN);
   }

   @Override
   public float sampleCoordinate(final DensitySampler coordinateSampler, final int coordinateIndex) {
      float cachedValue = this.cachedValues[coordinateIndex];
      if (!Float.isNaN(cachedValue)) {
         return cachedValue;
      } else {
         float value = coordinateSampler.sampleValue(this.context, this.blockX, this.blockY, this.blockZ);
         this.cachedValues[coordinateIndex] = value;
         return value;
      }
   }
}
