package net.minecraft.world.entity.monster.illager;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

class Vindicator$VindicatorJohnnyAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
   public Vindicator$VindicatorJohnnyAttackGoal(final Vindicator mob) {
      super(mob, LivingEntity.class, 0, true, true, (target, level) -> target.attackable());
   }

   @Override
   public boolean canUse() {
      return ((Vindicator)this.mob).isJohnny && super.canUse();
   }

   @Override
   public void start() {
      super.start();
      this.mob.setNoActionTime(0);
   }
}
