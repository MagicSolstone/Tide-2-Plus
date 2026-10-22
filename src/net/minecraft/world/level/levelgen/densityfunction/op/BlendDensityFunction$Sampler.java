package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

record BlendDensityFunction$Sampler(DensitySampler input) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.input.sampleVolume(context, outputBuffer, volume);
      Blender blender = context.getField(Blender.CONTEXT_KEY);
      if (blender != null && !blender.isEmpty()) {
         int index = 0;

         for (int z = 0; z < volume.sizeZ(); z++) {
            int blockZ = volume.blockZ(z);

            for (int x = 0; x < volume.sizeX(); x++) {
               int blockX = volume.blockX(x);

               for (int y = 0; y < volume.sizeY(); y++) {
                  int blockY = volume.blockY(y);
                  outputBuffer.set(index, blender.blendDensity(blockX, blockY, blockZ, outputBuffer.get(index)));
                  index++;
               }
            }
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      float input = this.input.sampleValue(context, blockX, blockY, blockZ);
      Blender blender = context.getField(Blender.CONTEXT_KEY);
      return blender != null && !blender.isEmpty() ? blender.blendDensity(blockX, blockY, blockZ, input) : input;
   }
}
