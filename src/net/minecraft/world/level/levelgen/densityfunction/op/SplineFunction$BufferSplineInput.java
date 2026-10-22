package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;

class SplineFunction$BufferSplineInput implements SplineFunction$SplineInput, AutoCloseable {
   private final SamplerContext context;
   private final DensityVolume volume;
   private final ScopedDensityBuffer[] buffers;
   private int index;

   private SplineFunction$BufferSplineInput(final SamplerContext context, final DensityVolume volume, final int coordinateCount) {
      this.context = context;
      this.volume = volume;
      this.buffers = new ScopedDensityBuffer[coordinateCount];
   }

   @Override
   public float sampleCoordinate(final DensitySampler coordinateSampler, final int coordinateIndex) {
      ScopedDensityBuffer buffer = this.buffers[coordinateIndex];
      if (buffer == null) {
         buffer = this.context.acquireBuffer(this.volume);
         coordinateSampler.sampleVolume(this.context, buffer, this.volume);
         this.buffers[coordinateIndex] = buffer;
      }

      return buffer.get(this.index);
   }

   @Override
   public void close() {
      for (ScopedDensityBuffer buffer : this.buffers) {
         if (buffer != null) {
            buffer.close();
         }
      }
   }
}
