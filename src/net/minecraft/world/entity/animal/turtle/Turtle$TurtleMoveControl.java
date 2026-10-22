package net.minecraft.world.entity.animal.turtle;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.MoveControl$Operation;

class Turtle$TurtleMoveControl<T extends Turtle> extends MoveControl<T> {
   public Turtle$TurtleMoveControl(final T turtle) {
      super(turtle);
   }

   private void updateSpeed() {
      if (this.mob.isInWater()) {
         this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, 0.005, 0.0));
         if (!this.mob.getHomePos().closerToCenterThan(this.mob.position(), 16.0)) {
            this.mob.setSpeed(Math.max(this.mob.getSpeed() / 2.0F, 0.08F));
         }

         if (this.mob.isBaby()) {
            this.mob.setSpeed(Math.max(this.mob.getSpeed() / 3.0F, 0.06F));
         }
      } else if (this.mob.onGround()) {
         this.mob.setSpeed(Math.max(this.mob.getSpeed() / 2.0F, 0.06F));
      }
   }

   @Override
   public void tick() {
      this.updateSpeed();
      if (this.operation == MoveControl$Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
         double xd = this.wantedX - this.mob.getX();
         double yd = this.wantedY - this.mob.getY();
         double zd = this.wantedZ - this.mob.getZ();
         double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
         if (dd < 1.0E-5F) {
            this.mob.setSpeed(0.0F);
         } else {
            yd /= dd;
            float yRotD = (float)(Mth.atan2(zd, xd) * 180.0F / (float)Math.PI) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yRotD, 90.0F));
            this.mob.yBodyRot = this.mob.getYRot();
            float targetSpeed = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            this.mob.setSpeed(Mth.lerp(0.125F, this.mob.getSpeed(), targetSpeed));
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, this.mob.getSpeed() * yd * 0.1, 0.0));
         }
      } else {
         this.mob.setSpeed(0.0F);
      }
   }
}
