package net.minecraft.world.level.levelgen.synth;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.IntStream;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.util.Interval;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

public final class NormalNoise {
   private static final double SAMPLE_COUNT = 2.0;
   private static final double INPUT_FACTOR = 1.0181268882175227;
   @VisibleForTesting
   public static final double TARGET_DEVIATION = 0.3333333333333333;
   private static final int MAX_OCTAVE_COUNT = 32;
   private static final double MAX_AMPLITUDE = 1000000.0;
   private static final double PERSISTENCE = 0.5;
   private static final double LACUNARITY = 2.0;
   public static final Codec<NormalNoise> DIRECT_CODEC = NormalNoise$Parameters.CODEC.xmap(NormalNoise::new, n -> n.parameters);
   public static final Codec<Holder<NormalNoise>> CODEC = RegistryCodecs.holder(Registries.NOISE, DIRECT_CODEC);
   private final NormalNoise$Parameters parameters;
   private final List<NormalNoise$OctaveInfo> octaves;
   private final double normalizationFactor;
   private final Interval range;

   private NormalNoise(final NormalNoise$Parameters parameters) {
      this.parameters = parameters;
      this.octaves = buildOctaves(
         parameters.baseOctave,
         parameters.baseAmplitude,
         parameters.octaveCount,
         parameters.normalize != NormalNoise$Normalization.DISABLED,
         parameters.amplitudeModifiers
      );
      double targetAmplitude = this.octaves.stream().mapToDouble(NormalNoise$OctaveInfo::absAmplitude).sum();
      double normalizationFactor = computeNormalizationFactor(targetAmplitude, this.octaves);
      if (parameters.normalize == NormalNoise$Normalization.LEGACY && normalizationFactor != 0.0) {
         double parityNormalizationFactor = computeParityNormalizationFactor(parameters.baseAmplitude, parameters.octaveCount, parameters.amplitudeModifiers);
         targetAmplitude *= parityNormalizationFactor / normalizationFactor;
         normalizationFactor = parityNormalizationFactor;
      }

      this.normalizationFactor = normalizationFactor;
      this.range = Interval.ofSymmetric((float)(targetAmplitude * 0.3333333333333333 * 6.0));
   }

   private static List<NormalNoise$OctaveInfo> buildOctaves(
      final int baseOctave, final double baseAmplitude, final int octaveCount, final boolean normalize, final DoubleList amplitudeModifiers
   ) {
      double frequency = Math.pow(2.0, baseOctave);
      double amplitude = baseAmplitude;
      if (normalize) {
         amplitude = baseAmplitude * (Math.pow(0.5, -(octaveCount - 1)) / (Math.pow(0.5, -octaveCount) - 1.0));
      }

      com.google.common.collect.ImmutableList.Builder<NormalNoise$OctaveInfo> octaves = ImmutableList.builderWithExpectedSize(octaveCount);

      for (int i = 0; i < octaveCount; i++) {
         double amplitudeModifier = getAmplitudeModifier(amplitudeModifiers, i);
         if (amplitudeModifier != 0.0) {
            double modifiedAmplitude = amplitude * amplitudeModifier;
            octaves.add(new NormalNoise$OctaveInfo(baseOctave + i, frequency, modifiedAmplitude));
         }

         frequency *= 2.0;
         amplitude *= 0.5;
      }

      return octaves.build();
   }

   private static double getAmplitudeModifier(final DoubleList amplitudeModifiers, final int index) {
      return amplitudeModifiers.isEmpty() ? 1.0 : amplitudeModifiers.getDouble(index);
   }

   private static double computeNormalizationFactor(final double targetAmplitude, final List<NormalNoise$OctaveInfo> octaves) {
      double inputDeviation = estimateDeviation(octaves);
      if (inputDeviation == 0.0) {
         return 0.0;
      } else {
         double inputSumDeviation = inputDeviation * Math.sqrt(2.0);
         double targetDeviation = targetAmplitude * 0.3333333333333333;
         return targetDeviation / inputSumDeviation;
      }
   }

   private static double estimateDeviation(final List<NormalNoise$OctaveInfo> octaves) {
      double variance = 0.0;

      for (NormalNoise$OctaveInfo octave : octaves) {
         double layerDeviation = 0.2702247831245211 * octave.absAmplitude();
         variance += Mth.square(layerDeviation);
      }

      return Math.sqrt(variance);
   }

   public static NormalNoise$Builder builder() {
      return new NormalNoise$Builder();
   }

   public static NormalNoise createParity(final int firstOctave, final DoubleList amplitudes) {
      if (amplitudes.isEmpty()) {
         throw new IllegalArgumentException("Need at least 1 amplitude");
      } else {
         NormalNoise$Builder parameters = builder();
         parameters.setBaseOctave(firstOctave);
         parameters.setOctaveCount(amplitudes.size());
         parameters.setBaseAmplitude(computeParityBaseAmplitude(parameters.baseOctave, amplitudes));

         for (int i = 0; i < amplitudes.size(); i++) {
            double modifier = amplitudes.getDouble(i);
            if (modifier != 1.0) {
               parameters.setAmplitudeModifier(i, modifier);
            }
         }

         return parameters.build();
      }
   }

   public static NormalNoise createParity(final int firstOctave, final double... amplitudes) {
      return createParity(firstOctave, DoubleList.of(amplitudes));
   }

   private static double computeParityBaseAmplitude(final int baseOctave, final DoubleList amplitudes) {
      List<NormalNoise$OctaveInfo> octaves = buildOctaves(baseOctave, 1.0, amplitudes.size(), true, amplitudes);
      double targetAmplitude = octaves.stream().mapToDouble(NormalNoise$OctaveInfo::absAmplitude).sum();
      double newNormalizationFactor = computeNormalizationFactor(targetAmplitude, octaves);
      if (newNormalizationFactor == 0.0) {
         return 1.0;
      } else {
         double oldNormalizationFactor = computeParityNormalizationFactor(1.0, amplitudes.size(), amplitudes);
         return oldNormalizationFactor / newNormalizationFactor;
      }
   }

   private static double computeParityNormalizationFactor(final double baseAmplitude, final int octaveCount, final DoubleList amplitudeModifiers) {
      int minOctave = Integer.MAX_VALUE;
      int maxOctave = Integer.MIN_VALUE;

      for (int i = 0; i < octaveCount; i++) {
         double modifier = getAmplitudeModifier(amplitudeModifiers, i);
         if (modifier != 0.0) {
            minOctave = Math.min(minOctave, i);
            maxOctave = Math.max(maxOctave, i);
         }
      }

      return baseAmplitude * 0.5 * 0.3333333333333333 / parityExpectedDeviation(maxOctave - minOctave);
   }

   private static double parityExpectedDeviation(final int octaveSpan) {
      return 0.1 * (1.0 + 1.0 / (octaveSpan + 1));
   }

   public Noise create(final RandomSource random) {
      PositionalRandomFactory firstRandom = random.forkPositional();
      PositionalRandomFactory secondRandom = random.forkPositional();
      NoiseStack$Builder stack = NoiseStack.builder();

      for (NormalNoise$OctaveInfo octave : this.octaves) {
         String octaveSeed = octave.seed();
         Noise firstNoise = new PerlinNoise(firstRandom.fromHashOf(octaveSeed));
         Noise secondNoise = new PerlinNoise(secondRandom.fromHashOf(octaveSeed));
         float valueFactor = (float)(this.normalizationFactor * octave.amplitude);
         stack.add(firstNoise, octave.frequency, valueFactor);
         stack.add(secondNoise, octave.frequency * 1.0181268882175227, valueFactor);
      }

      return stack.build();
   }

   @Deprecated
   public Noise createForLegacyNetherBiome(final RandomSource random) {
      DoubleList amplitudes = this.parameters.amplitudeModifiers();
      if (amplitudes.isEmpty()) {
         amplitudes = new DoubleArrayList(Collections.nCopies(this.parameters.octaveCount(), 1.0));
      }

      NoiseStack first = LegacyFbmInitializer.createForLegacyNetherBiome(random, this.parameters.baseOctave(), amplitudes);
      NoiseStack second = LegacyFbmInitializer.createForLegacyNetherBiome(random, this.parameters.baseOctave(), amplitudes);
      float valueFactor = (float)(this.normalizationFactor * this.parameters.baseAmplitude());
      return NoiseStack.builder().addStack(first, 1.0, valueFactor).addStack(second, 1.0181268882175227, valueFactor).build();
   }

   public Interval range() {
      return this.range;
   }

   @Override
   public boolean equals(final Object obj) {
      return obj == this ? true : obj instanceof NormalNoise noise && this.parameters.equals(noise.parameters);
   }

   @Override
   public int hashCode() {
      return this.parameters.hashCode();
   }

   @Override
   public String toString() {
      return "NormalNoise[parameters=" + this.parameters + "]";
   }

   @VisibleForTesting
   public void parityConfigString(final StringBuilder sb, final RandomSource random) {
      sb.append("NormalNoise {");
      sb.append("first: ");
      this.perlinParityConfigString(sb, random);
      sb.append(", second: ");
      this.perlinParityConfigString(sb, random);
      sb.append("}");
   }

   @VisibleForTesting
   private void perlinParityConfigString(final StringBuilder sb, final RandomSource random) {
      sb.append("PerlinNoise{");
      List<String> amplitudeStrings = IntStream.range(0, this.parameters.octaveCount)
         .mapToDouble(ix -> getAmplitudeModifier(this.parameters.amplitudeModifiers, ix))
         .mapToObj(d -> String.format(Locale.ROOT, "%.2f", d))
         .toList();
      sb.append("first octave: ").append(this.parameters.baseOctave).append(", amplitudes: ").append(amplitudeStrings).append(", noise levels: [");
      PositionalRandomFactory positional = random.forkPositional();

      for (int i = 0; i < this.parameters.octaveCount; i++) {
         sb.append(i).append(": ");
         int octaveIndex = this.parameters.baseOctave + i;
         Optional<NormalNoise$OctaveInfo> octave = this.octaves.stream().filter(info -> info.octaveIndex == octaveIndex).findFirst();
         if (octave.isPresent()) {
            new PerlinNoise(positional.fromHashOf(octave.get().seed())).parityConfigString(sb);
         } else {
            sb.append("null");
         }

         sb.append(", ");
      }

      sb.append("]");
      sb.append("}");
   }
}
