package net.minecraft.world.entity.animal.panda;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

class Panda$PandaAttackGoal extends MeleeAttackGoal {
   private final Panda panda;

   public Panda$PandaAttackGoal(final Panda mob, final double speedModifier, final boolean trackTarget) {
      super(mob, speedModifier, trackTarget);
      this.panda = mob;
   }

   @Override
   public boolean canUse() {
      return this.panda.canPerformAction() && super.canUse();
   }
}
