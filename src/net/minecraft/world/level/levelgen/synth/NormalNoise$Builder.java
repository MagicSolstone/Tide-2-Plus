package net.minecraft.world.level.levelgen.synth;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;

public class NormalNoise$Builder {
   private double baseAmplitude = 1.0;
   private int baseOctave;
   private int octaveCount = 1;
   private NormalNoise$Normalization normalize = NormalNoise$Normalization.ENABLED;
   private DoubleList amplitudeModifiers = DoubleList.of();

   private NormalNoise$Builder() {
   }

   public NormalNoise$Builder setBaseAmplitude(final double baseAmplitude) {
      this.baseAmplitude = baseAmplitude;
      return this;
   }

   public NormalNoise$Builder setBaseOctave(final int baseOctave) {
      this.baseOctave = baseOctave;
      return this;
   }

   public NormalNoise$Builder setOctaveCount(final int octaveCount) {
      if (!this.amplitudeModifiers.isEmpty()) {
         throw new IllegalArgumentException("Cannot set octave count after setting amplitude modifier");
      } else {
         this.octaveCount = octaveCount;
         return this;
      }
   }

   public NormalNoise$Builder setNormalize(final boolean normalize) {
      this.normalize = normalize ? NormalNoise$Normalization.ENABLED : NormalNoise$Normalization.DISABLED;
      return this;
   }

   @Deprecated
   public NormalNoise$Builder setLegacyNormalization() {
      this.normalize = NormalNoise$Normalization.LEGACY;
      return this;
   }

   public NormalNoise$Builder setAmplitudeModifier(final int index, final double value) {
      if (index >= 0 && index < this.octaveCount) {
         if (this.amplitudeModifiers.isEmpty()) {
            this.amplitudeModifiers = new DoubleArrayList(this.octaveCount);

            for (int i = 0; i < this.octaveCount; i++) {
               this.amplitudeModifiers.add(1.0);
            }
         }

         this.amplitudeModifiers.set(index, value);
         return this;
      } else {
         throw new IllegalArgumentException(index + " outside of octave range [0; " + this.octaveCount + ")");
      }
   }

   public NormalNoise build() {
      NormalNoise$Parameters parameters = new NormalNoise$Parameters(
         this.baseAmplitude, this.baseOctave, this.octaveCount, this.normalize, this.amplitudeModifiers
      );
      return new NormalNoise(parameters);
   }
}
