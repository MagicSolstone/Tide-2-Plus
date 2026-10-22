package net.minecraft.world.entity.monster.spider;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

class Spider$SpiderAttackGoal extends MeleeAttackGoal {
   public Spider$SpiderAttackGoal(final Spider mob) {
      super(mob, 1.0, true);
   }

   @Override
   public boolean canUse() {
      return super.canUse() && !this.mob.isVehicle();
   }

   @Override
   public boolean canContinueToUse() {
      float br = this.mob.getLightLevelDependentMagicValue();
      if (br >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
         this.mob.setTarget(null);
         return false;
      } else {
         return super.canContinueToUse();
      }
   }
}
