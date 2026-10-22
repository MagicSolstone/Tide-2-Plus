package net.minecraft.world.level.levelgen.densityfunction.generator;

import net.minecraft.core.Direction$Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

record GradientFunction$ClampedSampler(Direction$Axis axis, int fromCoordinate, int minCoordinate, int maxCoordinate, float fromValue, float coordinateFactor)
   implements GradientFunction$GradientSampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      GradientFunction$GradientSampler.sampleVolume(this, outputBuffer, volume);
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return this.compute(this.axis.choose(blockX, blockY, blockZ));
   }

   @Override
   public float compute(final int coordinate) {
      int relativeCoordinate = Mth.clamp(coordinate, this.minCoordinate, this.maxCoordinate) - this.fromCoordinate;
      return this.fromValue + relativeCoordinate * this.coordinateFactor;
   }
}
