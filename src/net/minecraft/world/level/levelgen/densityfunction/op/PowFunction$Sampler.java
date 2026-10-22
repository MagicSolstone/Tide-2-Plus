package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;

record PowFunction$Sampler(DensitySampler base, DensitySampler exponent) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.base.sampleVolume(context, outputBuffer, volume);

      try (ScopedDensityBuffer exponentBuffer = context.acquireBuffer(volume)) {
         this.exponent.sampleVolume(context, exponentBuffer, volume);

         for (int i = 0; i < outputBuffer.size(); i++) {
            float base = outputBuffer.get(i);
            float exponent = exponentBuffer.get(i);
            outputBuffer.set(i, (float)Math.pow(base, exponent));
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return (float)Math.pow(this.base.sampleValue(context, blockX, blockY, blockZ), this.exponent.sampleValue(context, blockX, blockY, blockZ));
   }
}
