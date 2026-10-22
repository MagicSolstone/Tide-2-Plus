package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;

public record SliceFunction$XSampler(DensitySampler input, int x) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      if (volume.sizeX() == 1 && volume.minBlockX() == this.x) {
         this.input.sampleVolume(context, outputBuffer, volume);
      } else {
         DensityVolume inputVolume = new DensityVolume(
            1, volume.sizeY(), volume.sizeZ(), this.x, volume.minBlockY(), volume.minBlockZ(), volume.stepBlockX(), volume.stepBlockY(), volume.stepBlockZ()
         );

         try (ScopedDensityBuffer inputBuffer = context.acquireBuffer(inputVolume)) {
            this.input.sampleVolume(context, inputBuffer, inputVolume);
            int index = 0;

            for (int z = 0; z < volume.sizeZ(); z++) {
               for (int x = 0; x < volume.sizeX(); x++) {
                  for (int y = 0; y < volume.sizeY(); y++) {
                     outputBuffer.set(index, inputBuffer.get(inputVolume.indexUnchecked(0, y, z)));
                     index++;
                  }
               }
            }
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return this.input.sampleValue(context, this.x, blockY, blockZ);
   }
}
