package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;

record IntervalSelectFunction$SingleThresholdSampler(DensitySampler input, float threshold, DensitySampler ifBelow, DensitySampler ifAbove)
   implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.input.sampleVolume(context, outputBuffer, volume);

      try (ScopedDensityBuffer ifBelowBuffer = context.acquireBuffer(volume)) {
         this.ifBelow.sampleVolume(context, ifBelowBuffer, volume);

         try (ScopedDensityBuffer ifAboveBuffer = context.acquireBuffer(volume)) {
            this.ifAbove.sampleVolume(context, ifAboveBuffer, volume);

            for (int i = 0; i < outputBuffer.size(); i++) {
               float input = outputBuffer.get(i);
               outputBuffer.set(i, input < this.threshold ? ifBelowBuffer.get(i) : ifAboveBuffer.get(i));
            }
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      float input = this.input.sampleValue(context, blockX, blockY, blockZ);
      return input < this.threshold ? this.ifBelow.sampleValue(context, blockX, blockY, blockZ) : this.ifAbove.sampleValue(context, blockX, blockY, blockZ);
   }
}
