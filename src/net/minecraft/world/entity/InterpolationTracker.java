package net.minecraft.world.entity;

import net.minecraft.world.phys.Vec3;

public interface InterpolationTracker {
   InterpolationTracker NO_OP = new InterpolationTracker$NoOpInterpolationTracker();

   void updateTracking(Vec3 trackingPos);

   PositionPath getPositionPath(Vec3 trackingPos);

   void clear();
}
