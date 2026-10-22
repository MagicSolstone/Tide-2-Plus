package net.minecraft.world.entity.monster.cubemob;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.MoveControl$Operation;

public class AbstractCubeMob$CubeMobMoveControl<T extends AbstractCubeMob> extends MoveControl<T> {
   private float yRot;
   private int jumpDelay;
   private boolean isAggressive;

   public AbstractCubeMob$CubeMobMoveControl(final T cubeMob) {
      super(cubeMob);
      this.yRot = 180.0F * cubeMob.getYRot() / (float) Math.PI;
   }

   public void setDirection(final float yRot, final boolean isAggressive) {
      this.yRot = yRot;
      this.isAggressive = isAggressive;
   }

   public void setWantedMovement(final double speedModifier) {
      this.speedModifier = speedModifier;
      this.operation = MoveControl$Operation.MOVE_TO;
   }

   @Override
   public void tick() {
      this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
      this.mob.yHeadRot = this.mob.getYRot();
      this.mob.yBodyRot = this.mob.getYRot();
      if (this.operation != MoveControl$Operation.MOVE_TO) {
         this.mob.setZza(0.0F);
      } else {
         this.operation = MoveControl$Operation.WAIT;
         if (this.mob.onGround()) {
            this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            if (this.jumpDelay-- <= 0) {
               this.jumpDelay = this.mob.getJumpDelay();
               if (this.isAggressive) {
                  this.jumpDelay /= 3;
               }

               this.mob.getJumpControl().jump();
               if (this.mob.doPlayJumpSound()) {
                  this.mob.playSound(this.mob.getJumpSound(), this.mob.getSoundVolume(), this.mob.getSoundPitch());
               }
            } else {
               this.mob.xxa = 0.0F;
               this.mob.zza = 0.0F;
               this.mob.setSpeed(0.0F);
            }
         } else {
            this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
         }
      }
   }
}
