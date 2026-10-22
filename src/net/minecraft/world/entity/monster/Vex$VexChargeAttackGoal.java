package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.phys.Vec3;

class Vex$VexChargeAttackGoal extends Goal {
   public Vex$VexChargeAttackGoal(final Vex this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      LivingEntity target = this.this$0.getTarget();
      return target != null && target.isAlive() && !this.this$0.getMoveControl().hasWanted() && Vex.access$000(this.this$0).nextInt(reducedTickDelay(7)) == 0
         ? this.this$0.distanceToSqr(target) > 4.0
         : false;
   }

   @Override
   public boolean canContinueToUse() {
      return this.this$0.getMoveControl().hasWanted() && this.this$0.isCharging() && this.this$0.getTarget() != null && this.this$0.getTarget().isAlive();
   }

   @Override
   public void start() {
      LivingEntity attackTarget = this.this$0.getTarget();
      if (attackTarget != null) {
         Vec3 eyePosition = attackTarget.getEyePosition();
         Vex.access$100(this.this$0).setWantedPosition(eyePosition.x, eyePosition.y, eyePosition.z, 1.0);
      }

      this.this$0.setIsCharging(true);
      this.this$0.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
   }

   @Override
   public void stop() {
      this.this$0.setIsCharging(false);
   }

   @Override
   public boolean requiresUpdateEveryTick() {
      return true;
   }

   @Override
   public void tick() {
      LivingEntity attackTarget = this.this$0.getTarget();
      if (attackTarget != null) {
         if (this.this$0.getBoundingBox().intersects(attackTarget.getBoundingBox())) {
            this.this$0.doHurtTarget(getServerLevel(this.this$0.level()), attackTarget);
            this.this$0.setIsCharging(false);
         } else {
            double distance = this.this$0.distanceToSqr(attackTarget);
            if (distance < 9.0) {
               Vec3 eyePosition = attackTarget.getEyePosition();
               Vex.access$200(this.this$0).setWantedPosition(eyePosition.x, eyePosition.y, eyePosition.z, 1.0);
            }
         }
      }
   }
}
