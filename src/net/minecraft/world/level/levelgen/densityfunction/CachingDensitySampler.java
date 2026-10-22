package net.minecraft.world.level.levelgen.densityfunction;

public record CachingDensitySampler(int id, DensitySampler input) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      context.sampleVolumeCached(this.id, this.input, outputBuffer, volume);
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return context.sampleValueCached(this.id, this.input, blockX, blockY, blockZ);
   }
}
