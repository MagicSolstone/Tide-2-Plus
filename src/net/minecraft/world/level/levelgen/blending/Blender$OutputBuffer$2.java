package net.minecraft.world.level.levelgen.blending;

import java.util.Objects;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

class Blender$OutputBuffer$2 implements DensitySampler {
   Blender$OutputBuffer$2(final Blender$OutputBuffer this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      DensitySampler.sampleVolumeNaive(context, outputBuffer, volume, this);
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return this.this$0.getOffset(blockX, blockZ);
   }
}
