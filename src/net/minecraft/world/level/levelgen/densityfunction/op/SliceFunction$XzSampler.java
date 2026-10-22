package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;

public record SliceFunction$XzSampler(DensitySampler input, int x, int z) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      if (volume.sizeX() == 1 && volume.sizeZ() == 1 && volume.minBlockX() == this.x && volume.minBlockZ() == this.z) {
         this.input.sampleVolume(context, outputBuffer, volume);
      } else {
         DensityVolume inputVolume = new DensityVolume(
            1, volume.sizeY(), 1, this.x, volume.minBlockY(), this.z, volume.stepBlockX(), volume.stepBlockY(), volume.stepBlockZ()
         );

         try (ScopedDensityBuffer inputBuffer = context.acquireBuffer(inputVolume)) {
            this.input.sampleVolume(context, inputBuffer, inputVolume);

            for (int y = 0; y < volume.sizeY(); y++) {
               float input = inputBuffer.get(inputVolume.indexUnchecked(0, y, 0));
               int index = volume.indexUnchecked(0, y, 0);

               for (int z = 0; z < volume.sizeZ(); z++) {
                  for (int x = 0; x < volume.sizeX(); x++) {
                     outputBuffer.set(index, input);
                     index += volume.sizeY();
                  }
               }
            }
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return this.input.sampleValue(context, this.x, blockY, this.z);
   }
}
