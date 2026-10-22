package net.minecraft.world.level.levelgen.densityfunction.generator;

import net.minecraft.core.Direction$Axis;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

record GradientFunction$MirroredRepeatSampler(Direction$Axis axis, int fromCoordinate, int coordinateRange, float fromValue, float coordinateFactor)
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
      int relativeCoordinate = coordinate - this.fromCoordinate;
      int tileIndex = Math.floorDiv(relativeCoordinate, this.coordinateRange);
      int localCoordinate = relativeCoordinate - tileIndex * this.coordinateRange;
      return (tileIndex & 1) == 0
         ? this.fromValue + localCoordinate * this.coordinateFactor
         : this.fromValue + (this.coordinateRange - localCoordinate) * this.coordinateFactor;
   }
}
