package net.minecraft.world.level.levelgen.synth;

import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;

class NoiseStack$Perlin extends NoiseStack {
   private NoiseStack$Perlin(final NoiseStack$Layer[] layers) {
      super(layers);
   }

   @Override
   public float get(final double x, final double y, final double z) {
      float value = 0.0F;

      for (NoiseStack$Layer layer : this.layers) {
         double frequency = layer.frequency;
         value += layer.amplitude * layer.noise.get(x * frequency, y * frequency, z * frequency);
      }

      return value;
   }

   @Override
   public void addToVolume(final DensityBuffer buffer, final DensityVolume volume, final double xzScale, final double yScale, final float amplitude) {
      for (NoiseStack$Layer layer : this.layers) {
         double frequency = layer.frequency;
         layer.noise.addToVolume(buffer, volume, xzScale * frequency, yScale * frequency, amplitude * layer.amplitude);
      }
   }
}
