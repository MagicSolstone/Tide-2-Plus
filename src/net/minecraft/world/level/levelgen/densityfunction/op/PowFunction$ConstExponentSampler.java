package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

record PowFunction$ConstExponentSampler(DensitySampler base, double exponent) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.base.sampleVolume(context, outputBuffer, volume);

      for (int i = 0; i < outputBuffer.size(); i++) {
         outputBuffer.set(i, (float)Math.pow(outputBuffer.get(i), this.exponent));
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return (float)Math.pow(this.base.sampleValue(context, blockX, blockY, blockZ), this.exponent);
   }
}
