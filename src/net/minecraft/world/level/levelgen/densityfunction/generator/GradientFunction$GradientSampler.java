package net.minecraft.world.level.levelgen.densityfunction.generator;

import net.minecraft.core.Direction$Axis;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;

interface GradientFunction$GradientSampler extends DensitySampler {
   static void sampleVolume(final GradientFunction$GradientSampler sampler, final DensityBuffer output, final DensityVolume volume) {
      switch (sampler.axis()) {
         case X:
            for (int x = 0; x < volume.sizeX(); x++) {
               float value = sampler.compute(volume.blockX(x));

               for (int z = 0; z < volume.sizeZ(); z++) {
                  output.setRange(volume.indexUnchecked(x, 0, z), volume.sizeY(), value);
               }
            }
            break;
         case Y:
            for (int y = 0; y < volume.sizeY(); y++) {
               float value = sampler.compute(volume.blockY(y));

               for (int z = 0; z < volume.sizeZ(); z++) {
                  for (int x = 0; x < volume.sizeX(); x++) {
                     output.set(volume.indexUnchecked(x, y, z), value);
                  }
               }
            }
            break;
         case Z:
            for (int z = 0; z < volume.sizeZ(); z++) {
               float value = sampler.compute(volume.blockZ(z));
               output.setRange(volume.indexUnchecked(0, 0, z), volume.sizeX() * volume.sizeY(), value);
            }
      }
   }

   Direction$Axis axis();

   float compute(int coordinate);
}
