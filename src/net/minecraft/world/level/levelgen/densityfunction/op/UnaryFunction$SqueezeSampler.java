package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

public record UnaryFunction$SqueezeSampler(DensitySampler input) implements DensitySampler {
   private static float apply(final float input) {
      float clampedInput = Mth.clamp(input, -1.0F, 1.0F);
      return clampedInput / 2.0F - Mth.cube(clampedInput) / 24.0F;
   }

   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.input.sampleVolume(context, outputBuffer, volume);

      for (int i = 0; i < outputBuffer.size(); i++) {
         outputBuffer.set(i, apply(outputBuffer.get(i)));
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return apply(this.input.sampleValue(context, blockX, blockY, blockZ));
   }
}
