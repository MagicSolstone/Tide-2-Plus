package net.minecraft.world.entity;

import net.minecraft.core.PositionAndRotation$Mutable;
import net.minecraft.world.phys.Vec3;

class SteppedInterpolationHandler$Step extends PositionAndRotation$Mutable {
   private final int tickOffset;

   public SteppedInterpolationHandler$Step(final Vec3 position, final float yRot, final float xRot, final int tickOffset) {
      this.tickOffset = tickOffset;
      this.set(position, yRot, xRot);
   }
}
