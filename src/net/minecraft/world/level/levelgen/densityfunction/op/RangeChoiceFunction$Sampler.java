package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;

record RangeChoiceFunction$Sampler(DensitySampler input, float minInclusive, float maxExclusive, DensitySampler whenInRange, DensitySampler whenOutOfRange)
   implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.whenInRange.sampleVolume(context, outputBuffer, volume);

      try (ScopedDensityBuffer inputBuffer = context.acquireBuffer(volume)) {
         this.input.sampleVolume(context, inputBuffer, volume);

         try (ScopedDensityBuffer whenOutOfRangeBuffer = context.acquireBuffer(volume)) {
            this.whenOutOfRange.sampleVolume(context, whenOutOfRangeBuffer, volume);

            for (int i = 0; i < outputBuffer.size(); i++) {
               float input = inputBuffer.get(i);
               if (!(input >= this.minInclusive) || !(input < this.maxExclusive)) {
                  outputBuffer.set(i, whenOutOfRangeBuffer.get(i));
               }
            }
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      float inputValue = this.input.sampleValue(context, blockX, blockY, blockZ);
      return inputValue >= this.minInclusive && inputValue < this.maxExclusive
         ? this.whenInRange.sampleValue(context, blockX, blockY, blockZ)
         : this.whenOutOfRange.sampleValue(context, blockX, blockY, blockZ);
   }
}
