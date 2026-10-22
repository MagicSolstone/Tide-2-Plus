package net.minecraft.world.entity.monster;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.MoveControl$Operation;
import net.minecraft.world.phys.Vec3;

class Guardian$GuardianMoveControl<T extends Guardian> extends MoveControl<T> {
   public Guardian$GuardianMoveControl(final T guardian) {
      super(guardian);
   }

   @Override
   public void tick() {
      if (this.operation == MoveControl$Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
         Vec3 delta = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
         double length = delta.length();
         double xd = delta.x / length;
         double yd = delta.y / length;
         double zd = delta.z / length;
         float yRotD = (float)(Mth.atan2(delta.z, delta.x) * 180.0F / (float)Math.PI) - 90.0F;
         this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yRotD, 90.0F));
         this.mob.yBodyRot = this.mob.getYRot();
         float targetSpeed = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
         float newSpeed = Mth.lerp(0.125F, this.mob.getSpeed(), targetSpeed);
         this.mob.setSpeed(newSpeed);
         double push = Math.sin((this.mob.tickCount + this.mob.getId()) * 0.5) * 0.05;
         double cos = Math.cos(this.mob.getYRot() * (float) (Math.PI / 180.0));
         double sin = Math.sin(this.mob.getYRot() * (float) (Math.PI / 180.0));
         double yPush = Math.sin((this.mob.tickCount + this.mob.getId()) * 0.75) * 0.05;
         this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(push * cos, yPush * (sin + cos) * 0.25 + newSpeed * yd * 0.1, push * sin));
         LookControl control = this.mob.getLookControl();
         double newLookX = this.mob.getX() + xd * 2.0;
         double newLookY = this.mob.getEyeY() + yd / length;
         double newLookZ = this.mob.getZ() + zd * 2.0;
         double oldLookX = control.getWantedX();
         double oldLookY = control.getWantedY();
         double oldLookZ = control.getWantedZ();
         if (!control.isLookingAtTarget()) {
            oldLookX = newLookX;
            oldLookY = newLookY;
            oldLookZ = newLookZ;
         }

         this.mob
            .getLookControl()
            .setLookAt(Mth.lerp(0.125, oldLookX, newLookX), Mth.lerp(0.125, oldLookY, newLookY), Mth.lerp(0.125, oldLookZ, newLookZ), 10.0F, 40.0F);
         this.mob.setMoving(true);
      } else {
         this.mob.setSpeed(0.0F);
         this.mob.setMoving(false);
      }
   }
}
