package net.minecraft.world.entity.animal.panda;

import net.minecraft.world.entity.ai.goal.Goal;

class Panda$PandaLieOnBackGoal extends Goal {
   private final Panda panda;
   private int cooldown;

   public Panda$PandaLieOnBackGoal(final Panda panda) {
      this.panda = panda;
   }

   @Override
   public boolean canUse() {
      return this.cooldown < this.panda.tickCount
         && this.panda.isLazy()
         && this.panda.canPerformAction()
         && Panda.access$800(this.panda).nextInt(reducedTickDelay(400)) == 1;
   }

   @Override
   public boolean canContinueToUse() {
      return !this.panda.isInWater() && (this.panda.isLazy() || Panda.access$900(this.panda).nextInt(reducedTickDelay(600)) != 1)
         ? Panda.access$1000(this.panda).nextInt(reducedTickDelay(2000)) != 1
         : false;
   }

   @Override
   public void start() {
      this.panda.setOnBack(true);
      this.cooldown = 0;
   }

   @Override
   public void stop() {
      this.panda.setOnBack(false);
      this.cooldown = this.panda.tickCount + 200;
   }
}
