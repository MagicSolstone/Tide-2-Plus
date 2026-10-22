package net.minecraft.world.entity.animal.fox;

import java.util.Objects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

class Fox$FoxMeleeAttackGoal extends MeleeAttackGoal {
   public Fox$FoxMeleeAttackGoal(final Fox this$0, final double speedModifier, final boolean trackTarget) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x, speedModifier, trackTarget);
   }

   @Override
   protected void checkAndPerformAttack(final LivingEntity target) {
      if (this.canPerformAttack(target)) {
         this.resetAttackCooldown();
         this.mob.doHurtTarget(getServerLevel(this.mob), target);
         this.this$0.playSound(SoundEvents.FOX_BITE, 1.0F, 1.0F);
      }
   }

   @Override
   public void start() {
      this.this$0.setIsInterested(false);
      super.start();
   }

   @Override
   public boolean canUse() {
      return !this.this$0.isSitting() && !this.this$0.isSleeping() && !this.this$0.isCrouching() && !this.this$0.isFaceplanted() && super.canUse();
   }
}
