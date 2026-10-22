package net.minecraft.world.entity.monster.spider;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

class Spider$SpiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
   public Spider$SpiderTargetGoal(final Spider mob, final Class<T> targetType) {
      super(mob, targetType, true);
   }

   @Override
   public boolean canUse() {
      float br = this.mob.getLightLevelDependentMagicValue();
      return br >= 0.5F ? false : super.canUse();
   }
}
