package net.minecraft.world.entity.animal.turtle;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.PanicGoal;

class Turtle$TurtlePanicGoal extends PanicGoal {
   public Turtle$TurtlePanicGoal(final Turtle turtle, final double speedModifier) {
      super(turtle, speedModifier);
   }

   @Override
   public boolean canUse() {
      if (!this.shouldPanic()) {
         return false;
      } else {
         BlockPos blockPos = this.lookForWater(this.mob.level(), this.mob, 7);
         if (blockPos != null) {
            this.posX = blockPos.getX();
            this.posY = blockPos.getY();
            this.posZ = blockPos.getZ();
            return true;
         } else {
            return this.findRandomPosition();
         }
      }
   }
}
