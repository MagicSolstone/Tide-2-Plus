package net.minecraft.world.level.redstone;

import net.minecraft.core.Direction;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class ExperimentalRedstoneUtils {
   @Nullable
   public static Orientation initialOrientation(final Level level, @Nullable final Direction front, @Nullable final Direction up) {
      if (level.enabledFeatures().contains(FeatureFlags.REDSTONE_EXPERIMENTS)) {
         Orientation orientation = Orientation.random(level.getRandom()).withSideBias(Orientation$SideBias.LEFT);
         if (up != null) {
            orientation = orientation.withUp(up);
         }

         if (front != null) {
            orientation = orientation.withFront(front);
         }

         return orientation;
      } else {
         return null;
      }
   }

   @Nullable
   public static Orientation withFront(@Nullable final Orientation orientation, final Direction front) {
      return orientation == null ? null : orientation.withFront(front);
   }
}
