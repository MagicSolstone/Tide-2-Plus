package net.minecraft.world.level.levelgen.densityfunction.generator;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.DistanceMetric;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

record DistanceToPointFunction$Sampler(int x, int y, int z, DistanceMetric metric) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      int index = 0;

      for (int z = 0; z < volume.sizeZ(); z++) {
         int blockZ = volume.blockZ(z);

         for (int x = 0; x < volume.sizeX(); x++) {
            int blockX = volume.blockX(x);

            for (int y = 0; y < volume.sizeY(); y++) {
               int blockY = volume.blockY(y);
               outputBuffer.set(index++, this.sampleValue(context, blockX, blockY, blockZ));
            }
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return this.metric.compute(this.x - blockX, this.y - blockY, this.z - blockZ);
   }
}
