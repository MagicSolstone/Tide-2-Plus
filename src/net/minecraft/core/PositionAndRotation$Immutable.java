package net.minecraft.core;

import net.minecraft.world.phys.Vec3;

public record PositionAndRotation$Immutable(Vec3 position, float yRot, float xRot) implements PositionAndRotation {
   @Override
   public PositionAndRotation immutable() {
      return this;
   }
}
