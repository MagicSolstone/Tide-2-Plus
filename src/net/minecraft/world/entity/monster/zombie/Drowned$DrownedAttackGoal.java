package net.minecraft.world.entity.monster.zombie;

import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;

class Drowned$DrownedAttackGoal extends ZombieAttackGoal {
   private final Drowned drowned;

   public Drowned$DrownedAttackGoal(final Drowned drowned, final double speedModifier, final boolean trackTarget) {
      super(drowned, speedModifier, trackTarget);
      this.drowned = drowned;
   }

   @Override
   public boolean canUse() {
      return super.canUse() && this.drowned.okTarget(this.drowned.getTarget());
   }

   @Override
   public boolean canContinueToUse() {
      return super.canContinueToUse() && this.drowned.okTarget(this.drowned.getTarget());
   }
}
