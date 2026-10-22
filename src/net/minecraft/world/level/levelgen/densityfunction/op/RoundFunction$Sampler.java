package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.densityfunction.ScopedDensityBuffer;

record RoundFunction$Sampler(RoundFunction$Type type, DensitySampler input, DensitySampler multiple) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.input.sampleVolume(context, outputBuffer, volume);

      try (ScopedDensityBuffer multipleBuffer = context.acquireBuffer(volume)) {
         this.multiple.sampleVolume(context, multipleBuffer, volume);

         for (int i = 0; i < outputBuffer.size(); i++) {
            float input = outputBuffer.get(i);
            float multiple = multipleBuffer.get(i);
            outputBuffer.set(i, this.apply(input, multiple));
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      float input = this.input.sampleValue(context, blockX, blockY, blockZ);
      float multiple = this.multiple.sampleValue(context, blockX, blockY, blockZ);
      return this.apply(input, multiple);
   }

   private float apply(final float input, final float multiple) {
      return multiple == 0.0F ? input : RoundFunction.roundToInteger(input / multiple, this.type) * multiple;
   }
}
