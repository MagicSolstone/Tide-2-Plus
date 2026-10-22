package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;

public record BinaryFunction$SubSampler(DensitySampler left, DensitySampler right) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.left.sampleVolume(context, outputBuffer, volume);

      try (ScopedDensityBuffer rightBuffer = context.acquireBuffer(volume)) {
         this.right.sampleVolume(context, rightBuffer, volume);

         for (int i = 0; i < outputBuffer.size(); i++) {
            outputBuffer.addTo(i, -rightBuffer.get(i));
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return this.left.sampleValue(context, blockX, blockY, blockZ) - this.right.sampleValue(context, blockX, blockY, blockZ);
   }
}
