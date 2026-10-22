package net.minecraft.world.entity.monster.zombie;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.MoveControl$Operation;

class Drowned$DrownedMoveControl<T extends Drowned> extends MoveControl<T> {
   public Drowned$DrownedMoveControl(final T drowned) {
      super(drowned);
   }

   @Override
   public void tick() {
      LivingEntity target = this.mob.getTarget();
      if (this.mob.wantsToSwim() && this.mob.isInWater()) {
         if (target != null && target.getY() > this.mob.getY() || this.mob.isSearchingForLand()) {
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, 0.002, 0.0));
         }

         if (this.operation != MoveControl$Operation.MOVE_TO || this.mob.getNavigation().isDone()) {
            this.mob.setSpeed(0.0F);
            return;
         }

         double xd = this.wantedX - this.mob.getX();
         double yd = this.wantedY - this.mob.getY();
         double zd = this.wantedZ - this.mob.getZ();
         double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
         yd /= dd;
         float yRotD = (float)(Mth.atan2(zd, xd) * 180.0F / (float)Math.PI) - 90.0F;
         this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yRotD, 90.0F));
         this.mob.yBodyRot = this.mob.getYRot();
         float targetSpeed = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
         float newSpeed = Mth.lerp(0.125F, this.mob.getSpeed(), targetSpeed);
         this.mob.setSpeed(newSpeed);
         this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(newSpeed * xd * 0.005, newSpeed * yd * 0.1, newSpeed * zd * 0.005));
      } else {
         if (!this.mob.onGround()) {
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, -0.008, 0.0));
         }

         super.tick();
      }
   }
}
