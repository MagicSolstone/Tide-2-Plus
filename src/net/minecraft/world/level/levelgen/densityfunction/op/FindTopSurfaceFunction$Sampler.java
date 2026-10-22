package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

record FindTopSurfaceFunction$Sampler(DensitySampler density, DensitySampler upperBound, int lowerBound, int cellHeight) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      if (volume.sizeY() != 1) {
         throw new IllegalArgumentException("Cannot sample with sizeY=" + volume.sizeY());
      } else {
         this.upperBound.sampleVolume(context, outputBuffer, volume);
         int index = 0;

         for (int z = 0; z < volume.sizeZ(); z++) {
            int blockZ = volume.blockZ(z);

            for (int x = 0; x < volume.sizeX(); x++) {
               int blockX = volume.blockX(x);
               float upperBound = outputBuffer.get(index);
               outputBuffer.set(index, this.findSurfaceFrom(context, blockX, blockZ, upperBound));
               index++;
            }
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      float upperBound = this.upperBound.sampleValue(context, blockX, blockY, blockZ);
      return this.findSurfaceFrom(context, blockX, blockZ, upperBound);
   }

   private int findSurfaceFrom(final SamplerContext context, final int x, final int z, final float upperBound) {
      int topY = Mth.floor(upperBound / this.cellHeight) * this.cellHeight;
      if (topY <= this.lowerBound) {
         return this.lowerBound;
      } else {
         for (int probeY = topY; probeY >= this.lowerBound; probeY -= this.cellHeight) {
            if (this.density.sampleValue(context, x, probeY, z) > 0.0F) {
               return probeY;
            }
         }

         return this.lowerBound;
      }
   }
}
