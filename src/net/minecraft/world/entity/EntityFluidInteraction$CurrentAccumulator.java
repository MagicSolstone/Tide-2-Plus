package net.minecraft.world.entity;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

class EntityFluidInteraction$CurrentAccumulator {
   private double height;
   private Vec3 accumulatedCurrent = Vec3.ZERO;
   private int currentCount;

   private EntityFluidInteraction$CurrentAccumulator() {
   }

   public void reset() {
      this.height = 0.0;
      this.accumulatedCurrent = Vec3.ZERO;
      this.currentCount = 0;
   }

   public void accumulate(final Vec3 flow) {
      this.accumulatedCurrent = this.accumulatedCurrent.add(flow);
      this.currentCount++;
   }

   public void applyTo(final Entity entity, final double scale) {
      if (this.currentCount != 0 && !(this.accumulatedCurrent.lengthSqr() < 1.0E-5F)) {
         Vec3 impulse;
         if (!(entity instanceof Player)) {
            impulse = this.accumulatedCurrent.normalize();
         } else {
            impulse = this.accumulatedCurrent.scale(1.0 / this.currentCount);
         }

         Vec3 oldMovement = entity.getDeltaMovement();
         impulse = impulse.scale(scale);
         double min = 0.003;
         if (Math.abs(oldMovement.x) < 0.003 && Math.abs(oldMovement.z) < 0.003 && impulse.length() < 0.0045000000000000005) {
            impulse = impulse.normalize().scale(0.0045000000000000005);
         }

         entity.addDeltaMovement(impulse);
      }
   }
}
