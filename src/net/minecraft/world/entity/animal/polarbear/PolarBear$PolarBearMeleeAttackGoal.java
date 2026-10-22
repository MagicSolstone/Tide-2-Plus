package net.minecraft.world.entity.animal.polarbear;

import java.util.Objects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

class PolarBear$PolarBearMeleeAttackGoal extends MeleeAttackGoal {
   public PolarBear$PolarBearMeleeAttackGoal(final PolarBear this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x, 1.25, true);
   }

   @Override
   protected void checkAndPerformAttack(final LivingEntity target) {
      if (this.canPerformAttack(target)) {
         this.resetAttackCooldown();
         this.mob.doHurtTarget(getServerLevel(this.mob), target);
         this.this$0.setStanding(false);
      } else if (this.mob.distanceToSqr(target) < (target.getBbWidth() + 3.0F) * (target.getBbWidth() + 3.0F)) {
         if (this.isTimeToAttack()) {
            this.this$0.setStanding(false);
            this.resetAttackCooldown();
         }

         if (this.getTicksUntilNextAttack() <= 10) {
            this.this$0.setStanding(true);
            this.this$0.playWarningSound();
         }
      } else {
         this.resetAttackCooldown();
         this.this$0.setStanding(false);
      }
   }

   @Override
   public void stop() {
      this.this$0.setStanding(false);
      super.stop();
   }
}
