package net.minecraft.world.entity.animal.panda;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

class Panda$PandaHurtByTargetGoal extends HurtByTargetGoal {
   private final Panda panda;

   public Panda$PandaHurtByTargetGoal(final Panda mob, final Class<?>... ignoreDamageFromTheseTypes) {
      super(mob, ignoreDamageFromTheseTypes);
      this.panda = mob;
   }

   @Override
   public boolean canContinueToUse() {
      if (!this.panda.gotBamboo && !this.panda.didBite) {
         return super.canContinueToUse();
      } else {
         this.panda.setTarget(null);
         return false;
      }
   }

   @Override
   protected void alertOther(final Mob other, final LivingEntity hurtByMob) {
      if (other instanceof Panda && other.isAggressive()) {
         other.setTarget(hurtByMob);
      }
   }
}
