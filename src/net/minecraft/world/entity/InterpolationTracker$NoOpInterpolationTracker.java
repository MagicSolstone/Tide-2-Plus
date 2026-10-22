package net.minecraft.world.entity;

import net.minecraft.world.phys.Vec3;

public record InterpolationTracker$NoOpInterpolationTracker() implements InterpolationTracker {
   @Override
   public void updateTracking(final Vec3 trackingPos) {
   }

   @Override
   public PositionPath getPositionPath(final Vec3 trackingPos) {
      return PositionPath.of(trackingPos);
   }

   @Override
   public void clear() {
   }
}
