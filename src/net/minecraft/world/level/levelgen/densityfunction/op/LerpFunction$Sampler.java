package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;

public record LerpFunction$Sampler(DensitySampler alpha, DensitySampler first, DensitySampler second) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.alpha.sampleVolume(context, outputBuffer, volume);

      try (ScopedDensityBuffer firstBuffer = context.acquireBuffer(volume)) {
         this.first.sampleVolume(context, firstBuffer, volume);

         try (ScopedDensityBuffer secondBuffer = context.acquireBuffer(volume)) {
            this.second.sampleVolume(context, secondBuffer, volume);

            for (int i = 0; i < outputBuffer.size(); i++) {
               float alpha = outputBuffer.get(i);
               float first = firstBuffer.get(i);
               float second = secondBuffer.get(i);
               if (alpha == 0.0F) {
                  outputBuffer.set(i, first);
               } else if (alpha == 1.0F) {
                  outputBuffer.set(i, second);
               } else {
                  outputBuffer.set(i, Mth.lerp(alpha, first, second));
               }
            }
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      float alpha = this.alpha.sampleValue(context, blockX, blockY, blockZ);
      if (alpha == 0.0F) {
         return this.first.sampleValue(context, blockX, blockY, blockZ);
      } else {
         return alpha == 1.0F
            ? this.second.sampleValue(context, blockX, blockY, blockZ)
            : Mth.lerp(alpha, this.first.sampleValue(context, blockX, blockY, blockZ), this.second.sampleValue(context, blockX, blockY, blockZ));
      }
   }
}
