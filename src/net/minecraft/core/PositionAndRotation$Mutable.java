package net.minecraft.core;

import net.minecraft.world.phys.Vec3;

public class PositionAndRotation$Mutable implements PositionAndRotation {
   private Vec3 position = Vec3.ZERO;
   private float yRot;
   private float xRot;

   @Override
   public Vec3 position() {
      return this.position;
   }

   @Override
   public float yRot() {
      return this.yRot;
   }

   @Override
   public float xRot() {
      return this.xRot;
   }

   @Override
   public PositionAndRotation immutable() {
      return PositionAndRotation.of(this.position, this.yRot, this.xRot);
   }

   public void set(final Vec3 position, final float yRot, final float xRot) {
      this.position = position;
      this.yRot = yRot;
      this.xRot = xRot;
   }

   public void addDelta(final Vec3 delta) {
      this.position = this.position.add(delta);
   }

   public void addRotation(final float yRot, final float xRot) {
      this.yRot += yRot;
      this.xRot += xRot;
   }
}
