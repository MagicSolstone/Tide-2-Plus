package net.minecraft.world.level.levelgen.densityfunction.generator;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.synth.Noise;

public record NoiseFunction$Sampler(Noise noise, double xzScale, double yScale) implements DensitySampler {
   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      outputBuffer.fill(0.0F);
      this.noise.addToVolume(outputBuffer, volume, this.xzScale, this.yScale, 1.0F);
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return this.noise.get(blockX * this.xzScale, blockY * this.yScale, blockZ * this.xzScale);
   }
}
