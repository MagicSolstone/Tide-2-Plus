package net.minecraft.world.entity.animal.fox;

import java.util.Objects;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class Fox$FoxPounceGoal extends JumpGoal {
   public Fox$FoxPounceGoal(final Fox this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public boolean canUse() {
      if (!this.this$0.isFullyCrouched()) {
         return false;
      } else {
         LivingEntity target = this.this$0.getTarget();
         if (target != null && target.isAlive()) {
            if (target.getMotionDirection() != target.getDirection()) {
               return false;
            } else {
               boolean hasClearPath = Fox.isPathClear(this.this$0, target);
               if (!hasClearPath) {
                  this.this$0.getNavigation().createPath(target, 0);
                  this.this$0.setIsCrouching(false);
                  this.this$0.setIsInterested(false);
               }

               return hasClearPath;
            }
         } else {
            return false;
         }
      }
   }

   @Override
   public boolean canContinueToUse() {
      LivingEntity target = this.this$0.getTarget();
      if (target != null && target.isAlive()) {
         double yd = this.this$0.getDeltaMovement().y;
         return (!(yd * yd < 0.05F) || !(Math.abs(this.this$0.getXRot()) < 15.0F) || !this.this$0.onGround()) && !this.this$0.isFaceplanted();
      } else {
         return false;
      }
   }

   @Override
   public boolean isInterruptable() {
      return false;
   }

   @Override
   public void start() {
      this.this$0.setJumping(true);
      this.this$0.setIsPouncing(true);
      this.this$0.setIsInterested(false);
      LivingEntity target = this.this$0.getTarget();
      if (target != null) {
         this.this$0.getLookControl().setLookAt(target, 60.0F, 30.0F);
         Vec3 uv = new Vec3(target.getX() - this.this$0.getX(), target.getY() - this.this$0.getY(), target.getZ() - this.this$0.getZ()).normalize();
         this.this$0.setDeltaMovement(this.this$0.getDeltaMovement().add(uv.x * 0.8, 0.9, uv.z * 0.8));
      }

      this.this$0.getNavigation().stop();
   }

   @Override
   public void stop() {
      this.this$0.setIsCrouching(false);
      this.this$0.crouchAmount = 0.0F;
      this.this$0.crouchAmountO = 0.0F;
      this.this$0.setIsInterested(false);
      this.this$0.setIsPouncing(false);
   }

   @Override
   public void tick() {
      LivingEntity target = this.this$0.getTarget();
      if (target != null) {
         this.this$0.getLookControl().setLookAt(target, 60.0F, 30.0F);
      }

      if (!this.this$0.isFaceplanted()) {
         Vec3 movement = this.this$0.getDeltaMovement();
         if (movement.y * movement.y < 0.03F && this.this$0.getXRot() != 0.0F) {
            this.this$0.setXRot(Mth.rotLerp(0.2F, this.this$0.getXRot(), 0.0F));
         } else {
            double direction = movement.horizontalDistance();
            float upwardsBias = Fox.access$400(this.this$0) && movement.y > 0.0 ? 6.5F : 1.0F;
            double biasedY = movement.y * upwardsBias;
            double len = Math.sqrt(direction * direction + biasedY * biasedY);
            if (len > 1.0E-5F) {
               double rotation = Math.signum(-biasedY) * Math.acos(direction / len) * 180.0F / (float)Math.PI;
               this.this$0.setXRot((float)rotation);
            }
         }
      }

      if (target != null && this.this$0.distanceTo(target) <= 2.0F) {
         this.this$0.doHurtTarget(getServerLevel(this.this$0.level()), target);
      } else if (this.this$0.getXRot() > 0.0F
         && this.this$0.onGround()
         && (float)this.this$0.getDeltaMovement().y != 0.0F
         && this.this$0.level().getBlockState(this.this$0.blockPosition()).is(Blocks.SNOW)) {
         this.this$0.setXRot(60.0F);
         this.this$0.setTarget(null);
         this.this$0.setFaceplanted(true);
      }
   }
}
