package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

record RangeChoiceFunction$ConstSampler(DensitySampler input, float minInclusive, float maxExclusive, float whenInRange, float whenOutOfRange)
   implements DensitySampler {
   private float choose(final float input) {
      return input >= this.minInclusive && input < this.maxExclusive ? this.whenInRange : this.whenOutOfRange;
   }

   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.input.sampleVolume(context, outputBuffer, volume);

      for (int i = 0; i < outputBuffer.size(); i++) {
         outputBuffer.set(i, this.choose(outputBuffer.get(i)));
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return this.choose(this.input.sampleValue(context, blockX, blockY, blockZ));
   }
}
