package net.minecraft.world.entity.animal.panda;

import net.minecraft.world.entity.ai.goal.Goal;

class Panda$PandaSneezeGoal extends Goal {
   private final Panda panda;

   public Panda$PandaSneezeGoal(final Panda panda) {
      this.panda = panda;
   }

   @Override
   public boolean canUse() {
      if (this.panda.isBaby() && this.panda.canPerformAction()) {
         return this.panda.isWeak() && Panda.access$200(this.panda).nextInt(reducedTickDelay(500)) == 1
            ? true
            : Panda.access$300(this.panda).nextInt(reducedTickDelay(6000)) == 1;
      } else {
         return false;
      }
   }

   @Override
   public boolean canContinueToUse() {
      return false;
   }

   @Override
   public void start() {
      this.panda.sneeze(true);
   }
}
