package net.minecraft.world.entity.animal.panda;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.ai.goal.PanicGoal;

class Panda$PandaPanicGoal extends PanicGoal {
   private final Panda panda;

   public Panda$PandaPanicGoal(final Panda mob, final double speedModifier) {
      super(mob, speedModifier, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES);
      this.panda = mob;
   }

   @Override
   public boolean canContinueToUse() {
      if (this.panda.isSitting()) {
         this.panda.getNavigation().stop();
         return false;
      } else {
         return super.canContinueToUse();
      }
   }
}
