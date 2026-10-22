package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

public record BinaryFunction$ConstMaxSampler(DensitySampler left, float right) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.left.sampleVolume(context, outputBuffer, volume);

      for (int i = 0; i < outputBuffer.size(); i++) {
         if (this.right > outputBuffer.get(i)) {
            outputBuffer.set(i, this.right);
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return Math.max(this.left.sampleValue(context, blockX, blockY, blockZ), this.right);
   }
}
