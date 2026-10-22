package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

record PowFunction$ConstBaseSampler(double base, DensitySampler exponent) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.exponent.sampleVolume(context, outputBuffer, volume);

      for (int i = 0; i < outputBuffer.size(); i++) {
         outputBuffer.set(i, (float)Math.pow(this.base, outputBuffer.get(i)));
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return (float)Math.pow(this.base, this.exponent.sampleValue(context, blockX, blockY, blockZ));
   }
}
