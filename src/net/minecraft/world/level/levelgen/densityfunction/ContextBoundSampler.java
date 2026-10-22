package net.minecraft.world.level.levelgen.densityfunction;

import net.minecraft.util.context.ContextKey;

public record ContextBoundSampler(ContextKey<? extends DensitySampler> key, DensitySampler fallbackSampler) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      DensitySampler sampler = context.getFieldOrDefault(this.key, this.fallbackSampler);
      sampler.sampleVolume(context, outputBuffer, volume);
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      DensitySampler sampler = context.getFieldOrDefault(this.key, this.fallbackSampler);
      return sampler.sampleValue(context, blockX, blockY, blockZ);
   }
}
