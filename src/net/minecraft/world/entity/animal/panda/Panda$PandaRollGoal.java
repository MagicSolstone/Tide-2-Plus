package net.minecraft.world.entity.animal.panda;

import java.util.EnumSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

class Panda$PandaRollGoal extends Goal {
   private final Panda panda;

   public Panda$PandaRollGoal(final Panda panda) {
      this.panda = panda;
      this.setFlags(EnumSet.of(Goal$Flag.MOVE, Goal$Flag.LOOK, Goal$Flag.JUMP));
   }

   @Override
   public boolean canUse() {
      if ((this.panda.isBaby() || this.panda.isPlayful()) && this.panda.onGround()) {
         if (!this.panda.canPerformAction()) {
            return false;
         } else {
            float angle = this.panda.getYRot() * (float) (Math.PI / 180.0);
            float xDir = -Mth.sin(angle);
            float zDir = Mth.cos(angle);
            int xStep = Math.abs(xDir) > 0.5 ? Mth.sign(xDir) : 0;
            int zStep = Math.abs(zDir) > 0.5 ? Mth.sign(zDir) : 0;
            if (this.panda.level().getBlockState(this.panda.blockPosition().offset(xStep, -1, zStep)).isAir()) {
               return true;
            } else {
               return this.panda.isPlayful() && Panda.access$000(this.panda).nextInt(reducedTickDelay(60)) == 1
                  ? true
                  : Panda.access$100(this.panda).nextInt(reducedTickDelay(500)) == 1;
            }
         }
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
      this.panda.roll(true);
   }

   @Override
   public boolean isInterruptable() {
      return false;
   }
}
