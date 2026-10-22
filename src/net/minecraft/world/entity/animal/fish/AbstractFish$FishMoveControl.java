package net.minecraft.world.entity.animal.fish;

import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.MoveControl$Operation;

class AbstractFish$FishMoveControl<T extends AbstractFish> extends MoveControl<T> {
   public AbstractFish$FishMoveControl(final T fish) {
      super(fish);
   }

   @Override
   public void tick() {
      if (this.mob.isEyeInFluid(FluidTags.WATER)) {
         this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, 0.005, 0.0));
      }

      if (this.operation == MoveControl$Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
         float targetSpeed = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
         this.mob.setSpeed(Mth.lerp(0.125F, this.mob.getSpeed(), targetSpeed));
         double xd = this.wantedX - this.mob.getX();
         double yd = this.wantedY - this.mob.getY();
         double zd = this.wantedZ - this.mob.getZ();
         if (yd != 0.0) {
            double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, this.mob.getSpeed() * (yd / dd) * 0.1, 0.0));
         }

         if (xd != 0.0 || zd != 0.0) {
            float yRotD = (float)(Mth.atan2(zd, xd) * 180.0F / (float)Math.PI) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yRotD, 90.0F));
            this.mob.yBodyRot = this.mob.getYRot();
         }
      } else {
         this.mob.setSpeed(0.0F);
      }
   }
}
