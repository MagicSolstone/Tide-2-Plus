package net.minecraft.world.entity.monster;

import java.util.Objects;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

class Phantom$PhantomMoveControl<T extends Mob> extends MoveControl<T> {
   private float speed;

   public Phantom$PhantomMoveControl(final T this$0, final Mob mob) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(mob);
      this.speed = 0.1F;
   }

   @Override
   public void tick() {
      if (this.this$0.horizontalCollision) {
         this.this$0.setYRot(this.this$0.getYRot() + 180.0F);
         this.speed = 0.1F;
      }

      double tdx = this.this$0.moveTargetPoint.x - this.this$0.getX();
      double tdy = this.this$0.moveTargetPoint.y - this.this$0.getY();
      double tdz = this.this$0.moveTargetPoint.z - this.this$0.getZ();
      double sd = Math.sqrt(tdx * tdx + tdz * tdz);
      if (Math.abs(sd) > 1.0E-5F) {
         double yRelativeScale = 1.0 - Math.abs(tdy * 0.7F) / sd;
         tdx *= yRelativeScale;
         tdz *= yRelativeScale;
         sd = Math.sqrt(tdx * tdx + tdz * tdz);
         double sd2 = Math.sqrt(tdx * tdx + tdz * tdz + tdy * tdy);
         float prev = this.this$0.getYRot();
         float angle = (float)Mth.atan2(tdz, tdx);
         float a = Mth.wrapDegrees(this.this$0.getYRot() + 90.0F);
         float b = Mth.wrapDegrees(angle * (180.0F / (float)Math.PI));
         this.this$0.setYRot(Mth.approachDegrees(a, b, 4.0F) - 90.0F);
         this.this$0.yBodyRot = this.this$0.getYRot();
         if (Mth.degreesDifferenceAbs(prev, this.this$0.getYRot()) < 3.0F) {
            this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
         } else {
            this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
         }

         float xRotD = (float)(-(Mth.atan2(-tdy, sd) * 180.0F / (float)Math.PI));
         this.this$0.setXRot(xRotD);
         float moveAngle = this.this$0.getYRot() + 90.0F;
         double txd = this.speed * Mth.cos(moveAngle * (float) (Math.PI / 180.0)) * Math.abs(tdx / sd2);
         double tzd = this.speed * Mth.sin(moveAngle * (float) (Math.PI / 180.0)) * Math.abs(tdz / sd2);
         double tyd = this.speed * Mth.sin(xRotD * (float) (Math.PI / 180.0)) * Math.abs(tdy / sd2);
         Vec3 movement = this.this$0.getDeltaMovement();
         this.this$0.setDeltaMovement(movement.add(new Vec3(txd, tyd, tzd).subtract(movement).scale(0.2)));
      }
   }
}
