package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

record RoundFunction$IntegerMultipleSampler(RoundFunction$Type type, DensitySampler input) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.input.sampleVolume(context, outputBuffer, volume);

      for (int i = 0; i < outputBuffer.size(); i++) {
         outputBuffer.set(i, RoundFunction.roundToInteger(outputBuffer.get(i), this.type));
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      float input = this.input.sampleValue(context, blockX, blockY, blockZ);
      return RoundFunction.roundToInteger(input, this.type);
   }
}
