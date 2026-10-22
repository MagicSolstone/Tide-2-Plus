package net.minecraft.world.level.levelgen.densityfunction;

public record DensitySampler$Bound(DensitySampler sampler, SamplerContext context) {
   public float sampleValue(final int blockX, final int blockY, final int blockZ) {
      return this.sampler.sampleValue(this.context, blockX, blockY, blockZ);
   }

   public void sampleVolume(final DensityBuffer outputBuffer, final DensityVolume volume) {
      this.sampler.sampleVolume(this.context, outputBuffer, volume);
   }

   public ScopedDensityBuffer sampleVolume(final DensityVolume volume) {
      ScopedDensityBuffer buffer = this.context.acquireBuffer(volume);
      this.sampleVolume(buffer, volume);
      return buffer;
   }
}
