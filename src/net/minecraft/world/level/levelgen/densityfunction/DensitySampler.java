package net.minecraft.world.level.levelgen.densityfunction;

public interface DensitySampler {
   void sampleVolume(SamplerContext context, DensityBuffer outputBuffer, DensityVolume volume);

   static void sampleVolumeNaive(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume, final DensitySampler sampler) {
      int index = 0;

      for (int z = 0; z < volume.sizeZ(); z++) {
         int blockZ = volume.blockZ(z);

         for (int x = 0; x < volume.sizeX(); x++) {
            int blockX = volume.blockX(x);

            for (int y = 0; y < volume.sizeY(); y++) {
               int blockY = volume.blockY(y);
               outputBuffer.set(index++, sampler.sampleValue(context, blockX, blockY, blockZ));
            }
         }
      }
   }

   float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ);

   default DensitySampler$Bound bind(final SamplerContext context) {
      return new DensitySampler$Bound(this, context);
   }
}
