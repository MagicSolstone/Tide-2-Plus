package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;

record IntervalSelectFunction$Sampler(DensitySampler input, float[] thresholds, DensitySampler[] samplers) implements DensitySampler {
   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.input.sampleVolume(context, outputBuffer, volume);
      ScopedDensityBuffer[] buffers = new ScopedDensityBuffer[this.samplers.length];
      boolean var15 = false /* VF: Semaphore variable */;

      try {
         var15 = true;

         for (int i = 0; i < this.samplers.length; i++) {
            ScopedDensityBuffer buffer = context.acquireBuffer(volume);
            buffers[i] = buffer;
            this.samplers[i].sampleVolume(context, buffer, volume);
         }

         for (int i = 0; i < outputBuffer.size(); i++) {
            int samplerIndex = this.selectSamplerIndex(outputBuffer.get(i));
            outputBuffer.set(i, buffers[samplerIndex].get(i));
         }

         var15 = false;
      } finally {
         if (var15) {
            ScopedDensityBuffer[] var10 = buffers;
            int var11 = buffers.length;
            int var12 = 0;

            while (true) {
               if (var12 >= var11) {
                  ;
               } else {
                  ScopedDensityBuffer buffer = var10[var12];
                  if (buffer != null) {
                     buffer.close();
                  }

                  var12++;
               }
            }
         }
      }

      for (ScopedDensityBuffer buffer : buffers) {
         if (buffer != null) {
            buffer.close();
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      float input = this.input.sampleValue(context, blockX, blockY, blockZ);
      return this.samplers[this.selectSamplerIndex(input)].sampleValue(context, blockX, blockY, blockZ);
   }

   private int selectSamplerIndex(final float input) {
      for (int i = 0; i < this.thresholds.length; i++) {
         if (input < this.thresholds[i]) {
            return i;
         }
      }

      return this.samplers.length - 1;
   }
}
