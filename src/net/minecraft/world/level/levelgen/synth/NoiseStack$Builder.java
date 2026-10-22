package net.minecraft.world.level.levelgen.synth;

import java.util.ArrayList;
import java.util.List;

public class NoiseStack$Builder {
   private final List<NoiseStack$Layer> layers = new ArrayList<>();

   private NoiseStack$Builder() {
   }

   public NoiseStack$Builder add(final Noise noise, final double frequency, final float amplitude) {
      this.layers.add(new NoiseStack$Layer(noise, frequency, amplitude));
      return this;
   }

   public NoiseStack$Builder addStack(final NoiseStack stack, final double frequency, final float amplitude) {
      for (NoiseStack$Layer layer : stack.layers) {
         this.layers.add(new NoiseStack$Layer(layer.noise, layer.frequency * frequency, layer.amplitude * amplitude));
      }

      return this;
   }

   public NoiseStack build() {
      NoiseStack$Layer[] layers = this.layers.toArray(NoiseStack$Layer[]::new);
      if (this.layers.stream().allMatch(layer -> layer.noise.getClass() == PerlinNoise.class)) {
         return new NoiseStack$Perlin(layers);
      } else {
         return (NoiseStack)(this.layers.stream().allMatch(layer -> layer.noise.getClass() == SmearedPerlinNoise.class)
            ? new NoiseStack$SmearedPerlin(layers)
            : new NoiseStack(layers));
      }
   }
}
