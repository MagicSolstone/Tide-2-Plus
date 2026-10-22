package net.minecraft.world.level.levelgen.densityfunction.generator;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

record EndIslandFunction$Sampler(SimplexNoise islandNoise) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      for (int z = 0; z < volume.sizeZ(); z++) {
         int blockZ = volume.blockZ(z);

         for (int x = 0; x < volume.sizeX(); x++) {
            int blockX = volume.blockX(x);
            float value = this.sampleValue(context, blockX, 0, blockZ);
            int index = volume.indexUnchecked(x, 0, z);
            outputBuffer.setRange(index, volume.sizeY(), value);
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return (EndIslandFunction.getHeightValue(this.islandNoise, blockX / 8, blockZ / 8) - 8.0F) / 128.0F;
   }
}
