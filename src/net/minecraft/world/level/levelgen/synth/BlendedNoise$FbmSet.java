package net.minecraft.world.level.levelgen.synth;

import com.google.common.annotations.VisibleForTesting;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public record BlendedNoise$FbmSet(NoiseStack minLimitNoise, NoiseStack maxLimitNoise, NoiseStack mainNoise) {
   @VisibleForTesting
   public void parityConfigString(final StringBuilder sb) {
      sb.append("BlendedNoise{minLimitNoise=");
      this.parityConfigString(sb, this.minLimitNoise);
      sb.append(", maxLimitNoise=");
      this.parityConfigString(sb, this.maxLimitNoise);
      sb.append(", mainNoise=");
      this.parityConfigString(sb, this.mainNoise);
      sb.append(
            String.format(
               Locale.ROOT,
               ", xzScale=%.3f, yScale=%.3f, xzMainScale=%.3f, yMainScale=%.3f, cellWidth=4, cellHeight=8",
               684.412,
               684.412,
               8.555150000000001,
               4.277575000000001
            )
         )
         .append('}');
   }

   private void parityConfigString(final StringBuilder output, final NoiseStack noise) {
      int octaves = noise.layers.length;
      output.append("PerlinNoise{");
      List<String> amplitudeStrings = IntStream.range(0, octaves).mapToObj(var0 -> "1.00").toList();
      output.append("first octave: ").append(-octaves + 1).append(", amplitudes: ").append(amplitudeStrings).append(", noise levels: [");

      for (int i = 0; i < octaves; i++) {
         output.append(i).append(": ");
         ((SmearedPerlinNoise)noise.getLayer(octaves - 1 - i)).parityConfigString(output);
         output.append(", ");
      }

      output.append("]");
      output.append("}");
   }
}
