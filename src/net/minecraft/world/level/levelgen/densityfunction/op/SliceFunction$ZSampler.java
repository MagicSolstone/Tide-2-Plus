package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;

public record SliceFunction$ZSampler(DensitySampler input, int z) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      if (volume.sizeZ() == 1 && volume.minBlockZ() == this.z) {
         this.input.sampleVolume(context, outputBuffer, volume);
      } else {
         DensityVolume inputVolume = new DensityVolume(
            volume.sizeX(), volume.sizeY(), 1, volume.minBlockX(), volume.minBlockY(), this.z, volume.stepBlockX(), volume.stepBlockY(), volume.stepBlockZ()
         );

         try (ScopedDensityBuffer inputBuffer = context.acquireBuffer(inputVolume)) {
            this.input.sampleVolume(context, inputBuffer, inputVolume);
            int index = 0;

            for (int z = 0; z < volume.sizeZ(); z++) {
               for (int x = 0; x < volume.sizeX(); x++) {
                  for (int y = 0; y < volume.sizeY(); y++) {
                     outputBuffer.set(index, inputBuffer.get(inputVolume.indexUnchecked(x, y, 0)));
                     index++;
                  }
               }
            }
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return this.input.sampleValue(context, blockX, blockY, this.z);
   }
}
