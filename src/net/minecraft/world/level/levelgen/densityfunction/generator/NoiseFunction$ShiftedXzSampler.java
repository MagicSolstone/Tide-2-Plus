package net.minecraft.world.level.levelgen.densityfunction.generator;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;
import net.minecraft.world.level.levelgen.synth.Noise;

public record NoiseFunction$ShiftedXzSampler(DensitySampler shiftX, DensitySampler shiftZ, Noise noise, double xzScale, double yScale) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.shiftX.sampleVolume(context, outputBuffer, volume);

      try (ScopedDensityBuffer shiftZBuffer = context.acquireBuffer(volume)) {
         this.shiftZ.sampleVolume(context, shiftZBuffer, volume);
         int index = 0;

         for (int z = 0; z < volume.sizeZ(); z++) {
            double baseNoiseZ = volume.blockZ(z) * this.xzScale;

            for (int x = 0; x < volume.sizeX(); x++) {
               double baseNoiseX = volume.blockX(x) * this.xzScale;

               for (int y = 0; y < volume.sizeY(); y++) {
                  double noiseX = baseNoiseX + outputBuffer.get(index);
                  double noiseY = volume.blockY(y) * this.yScale;
                  double noiseZ = baseNoiseZ + shiftZBuffer.get(index);
                  outputBuffer.set(index, this.noise.get(noiseX, noiseY, noiseZ));
                  index++;
               }
            }
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      double x = blockX * this.xzScale + this.shiftX.sampleValue(context, blockX, blockY, blockZ);
      double y = blockY * this.yScale;
      double z = blockZ * this.xzScale + this.shiftZ.sampleValue(context, blockX, blockY, blockZ);
      return this.noise.get(x, y, z);
   }
}
