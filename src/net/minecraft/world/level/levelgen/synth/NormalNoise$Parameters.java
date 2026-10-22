package net.minecraft.world.level.levelgen.synth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import java.util.function.Function;

record NormalNoise$Parameters(double baseAmplitude, int baseOctave, int octaveCount, NormalNoise$Normalization normalize, DoubleList amplitudeModifiers) {
   private static final Codec<DoubleList> AMPLITUDE_MODIFIERS_CODEC = Codec.doubleRange(0.0, 1000000.0)
      .listOf(0, 32)
      .xmap(DoubleArrayList::new, Function.identity());
   public static final Codec<NormalNoise$Parameters> CODEC = RecordCodecBuilder.create(
         i -> i.group(
               Codec.doubleRange(1.0E-5F, 1000000.0).optionalFieldOf("base_amplitude", 1.0).forGetter(NormalNoise$Parameters::baseAmplitude),
               Codec.intRange(-32, 32).fieldOf("base_octave").forGetter(NormalNoise$Parameters::baseOctave),
               Codec.intRange(1, 32).optionalFieldOf("octave_count", 1).forGetter(NormalNoise$Parameters::octaveCount),
               NormalNoise$Normalization.CODEC.optionalFieldOf("normalize", NormalNoise$Normalization.ENABLED).forGetter(NormalNoise$Parameters::normalize),
               AMPLITUDE_MODIFIERS_CODEC.optionalFieldOf("amplitude_modifiers", DoubleList.of()).forGetter(NormalNoise$Parameters::amplitudeModifiers)
            )
            .apply(i, NormalNoise$Parameters::new)
      )
      .validate(NormalNoise$Parameters::validate);

   private static DataResult<NormalNoise$Parameters> validate(final NormalNoise$Parameters parameters) {
      return !parameters.amplitudeModifiers().isEmpty() && parameters.amplitudeModifiers().size() != parameters.octaveCount()
         ? DataResult.error(
            () -> "amplitude_modifiers had size " + parameters.amplitudeModifiers().size() + ", but octave_count was " + parameters.octaveCount()
         )
         : DataResult.success(parameters);
   }
}
