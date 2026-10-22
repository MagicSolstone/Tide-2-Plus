package net.minecraft.world.entity;

import net.minecraft.core.PositionAndRotation;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public interface InterpolationHandler {
   InterpolationHandler NO_OP = new InterpolationHandler$NoOpInterpolationHandler();

   default InterpolationTracker interpolationTracker() {
      return InterpolationTracker.NO_OP;
   }

   @Nullable
   PositionAndRotation target();

   boolean interpolateTo(@Nullable PositionPath position, float yRot, float xRot, boolean hasRotation);

   void interpolate();

   void applyPredictedMovement(Vec3 delta);

   boolean hasActiveInterpolation();

   void cancel();
}
