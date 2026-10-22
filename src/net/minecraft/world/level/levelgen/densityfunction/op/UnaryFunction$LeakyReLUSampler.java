package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

public record UnaryFunction$LeakyReLUSampler(DensitySampler input, float negativeFactor) implements DensitySampler {
   private static float apply(final float negativeFactor, final float input) {
      return input > 0.0F ? input : input * negativeFactor;
   }

   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.input.sampleVolume(context, outputBuffer, volume);

      for (int i = 0; i < outputBuffer.size(); i++) {
         outputBuffer.set(i, apply(this.negativeFactor, outputBuffer.get(i)));
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return apply(this.negativeFactor, this.input.sampleValue(context, blockX, blockY, blockZ));
   }
}
