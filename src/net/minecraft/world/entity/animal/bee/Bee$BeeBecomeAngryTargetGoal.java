package net.minecraft.world.entity.animal.bee;

import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

class Bee$BeeBecomeAngryTargetGoal extends NearestAttackableTargetGoal<Player> {
   public Bee$BeeBecomeAngryTargetGoal(final Bee bee) {
      super(bee, Player.class, 10, true, false, bee::isAngryAt);
   }

   @Override
   public boolean canUse() {
      return this.beeCanTarget() && super.canUse();
   }

   @Override
   public boolean canContinueToUse() {
      boolean beeCanTarget = this.beeCanTarget();
      if (beeCanTarget && this.mob.getTarget() != null) {
         return super.canContinueToUse();
      } else {
         this.targetMob = null;
         return false;
      }
   }

   private boolean beeCanTarget() {
      Bee bee = (Bee)this.mob;
      return bee.isAngry() && !bee.hasStung();
   }
}
