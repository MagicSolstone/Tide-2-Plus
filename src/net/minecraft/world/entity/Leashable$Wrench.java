package net.minecraft.world.entity;

import java.util.List;
import net.minecraft.world.phys.Vec3;

public record Leashable$Wrench(Vec3 force, double torque) {
   public static final Leashable$Wrench ZERO = new Leashable$Wrench(Vec3.ZERO, 0.0);

   public static double torqueFromForce(final Vec3 leverArm, final Vec3 force) {
      return leverArm.z * force.x - leverArm.x * force.z;
   }

   public static Leashable$Wrench accumulate(final List<Leashable$Wrench> wrenches) {
      if (wrenches.isEmpty()) {
         return ZERO;
      } else {
         double x = 0.0;
         double y = 0.0;
         double z = 0.0;
         double t = 0.0;

         for (Leashable$Wrench wrench : wrenches) {
            Vec3 force = wrench.force;
            x += force.x;
            y += force.y;
            z += force.z;
            t += wrench.torque;
         }

         return new Leashable$Wrench(new Vec3(x, y, z), t);
      }
   }

   public Leashable$Wrench scale(final double scale) {
      return new Leashable$Wrench(this.force.scale(scale), this.torque * scale);
   }
}
