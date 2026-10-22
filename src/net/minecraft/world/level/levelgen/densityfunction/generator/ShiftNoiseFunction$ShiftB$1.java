package net.minecraft.world.level.levelgen.densityfunction.generator;

import java.util.Objects;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;
import net.minecraft.world.level.levelgen.synth.Noise;

class ShiftNoiseFunction$ShiftB$1 implements DensitySampler {
   ShiftNoiseFunction$ShiftB$1(final ShiftNoiseFunction$ShiftB this$0, final Noise val$noise) {
      this.val$noise = val$noisex;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      DensityVolume transposedVolume = new DensityVolume(
         volume.sizeZ(), volume.sizeX(), 1, volume.minBlockZ(), volume.minBlockX(), 0, volume.stepBlockZ(), volume.stepBlockX(), 1
      );

      try (ScopedDensityBuffer transposedBuffer = context.acquireBuffer(transposedVolume)) {
         transposedBuffer.fill(0.0F);
         this.val$noise.addToVolume(transposedBuffer, transposedVolume, 0.25, 0.25, 4.0F);

         for (int z = 0; z < volume.sizeZ(); z++) {
            for (int x = 0; x < volume.sizeX(); x++) {
               float value = transposedBuffer.get(transposedVolume.indexUnchecked(z, x, 0));
               outputBuffer.setRange(volume.indexUnchecked(x, 0, z), volume.sizeY(), value);
            }
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return this.val$noise.get(blockZ * 0.25, blockX * 0.25, 0.0) * 4.0F;
   }
}
