package net.minecraft.world.level.biome;

import com.google.common.annotations.VisibleForTesting;

public class Climate {
   private static final boolean DEBUG_SLOW_BIOME_SEARCH = false;
   private static final float QUANTIZATION_FACTOR = 10000.0F;
   @VisibleForTesting
   protected static final int PARAMETER_COUNT = 7;

   public static Climate$TargetPoint target(
      final float temperature, final float humidity, final float continentalness, final float erosion, final float depth, final float weirdness
   ) {
      return new Climate$TargetPoint(
         quantizeCoord(temperature),
         quantizeCoord(humidity),
         quantizeCoord(continentalness),
         quantizeCoord(erosion),
         quantizeCoord(depth),
         quantizeCoord(weirdness)
      );
   }

   public static Climate$ParameterPoint parameters(
      final float temperature,
      final float humidity,
      final float continentalness,
      final float erosion,
      final float depth,
      final float weirdness,
      final float offset
   ) {
      return new Climate$ParameterPoint(
         Climate$Parameter.point(temperature),
         Climate$Parameter.point(humidity),
         Climate$Parameter.point(continentalness),
         Climate$Parameter.point(erosion),
         Climate$Parameter.point(depth),
         Climate$Parameter.point(weirdness),
         quantizeCoord(offset)
      );
   }

   public static Climate$ParameterPoint parameters(
      final Climate$Parameter temperature,
      final Climate$Parameter humidity,
      final Climate$Parameter continentalness,
      final Climate$Parameter erosion,
      final Climate$Parameter depth,
      final Climate$Parameter weirdness,
      final float offset
   ) {
      return new Climate$ParameterPoint(temperature, humidity, continentalness, erosion, depth, weirdness, quantizeCoord(offset));
   }

   public static long quantizeCoord(final float coord) {
      return (long)(coord * 10000.0F);
   }

   public static float unquantizeCoord(final long coord) {
      return (float)coord / 10000.0F;
   }
}
