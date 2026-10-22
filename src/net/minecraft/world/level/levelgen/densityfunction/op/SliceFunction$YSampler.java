package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;

public record SliceFunction$YSampler(DensitySampler input, int y) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      if (volume.sizeY() == 1 && volume.minBlockY() == this.y) {
         this.input.sampleVolume(context, outputBuffer, volume);
      } else {
         DensityVolume inputVolume = new DensityVolume(
            volume.sizeX(), 1, volume.sizeZ(), volume.minBlockX(), this.y, volume.minBlockZ(), volume.stepBlockX(), volume.stepBlockY(), volume.stepBlockZ()
         );

         try (ScopedDensityBuffer inputBuffer = context.acquireBuffer(inputVolume)) {
            this.input.sampleVolume(context, inputBuffer, inputVolume);

            for (int z = 0; z < volume.sizeZ(); z++) {
               for (int x = 0; x < volume.sizeX(); x++) {
                  float input = inputBuffer.get(inputVolume.indexUnchecked(x, 0, z));
                  outputBuffer.setRange(volume.indexUnchecked(x, 0, z), volume.sizeY(), input);
               }
            }
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return this.input.sampleValue(context, blockX, this.y, blockZ);
   }
}
