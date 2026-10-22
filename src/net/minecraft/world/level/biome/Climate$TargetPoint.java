package net.minecraft.world.level.biome;

import com.google.common.annotations.VisibleForTesting;

public record Climate$TargetPoint(long temperature, long humidity, long continentalness, long erosion, long depth, long weirdness) {
   @VisibleForTesting
   long[] toParameterArray() {
      return new long[]{this.temperature, this.humidity, this.continentalness, this.erosion, this.depth, this.weirdness, 0L};
   }
}
