package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class Camera$NearPlane {
   private final Vec3 forward;
   private final Vec3 left;
   private final Vec3 up;

   private Camera$NearPlane(final Vec3 forward, final Vec3 left, final Vec3 up) {
      this.forward = forward;
      this.left = left;
      this.up = up;
   }

   public Vec3 getTopLeft() {
      return this.forward.add(this.up).add(this.left);
   }

   public Vec3 getTopRight() {
      return this.forward.add(this.up).subtract(this.left);
   }

   public Vec3 getBottomLeft() {
      return this.forward.subtract(this.up).add(this.left);
   }

   public Vec3 getBottomRight() {
      return this.forward.subtract(this.up).subtract(this.left);
   }

   public Vec3 getPointOnPlane(final float x, final float y) {
      return this.forward.add(this.up.scale(y)).subtract(this.left.scale(x));
   }
}
