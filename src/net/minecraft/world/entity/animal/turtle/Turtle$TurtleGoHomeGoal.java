package net.minecraft.world.entity.animal.turtle;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

class Turtle$TurtleGoHomeGoal extends Goal {
   private final Turtle turtle;
   private final double speedModifier;
   private boolean stuck;
   private int closeToHomeTryTicks;
   private static final int GIVE_UP_TICKS = 600;

   public Turtle$TurtleGoHomeGoal(final Turtle turtle, final double speedModifier) {
      this.turtle = turtle;
      this.speedModifier = speedModifier;
   }

   @Override
   public boolean canUse() {
      if (this.turtle.isBaby()) {
         return false;
      } else if (this.turtle.hasEgg()) {
         return true;
      } else {
         return this.turtle.getRandom().nextInt(reducedTickDelay(700)) != 0 ? false : !this.turtle.homePos.closerToCenterThan(this.turtle.position(), 64.0);
      }
   }

   @Override
   public void start() {
      this.turtle.goingHome = true;
      this.stuck = false;
      this.closeToHomeTryTicks = 0;
   }

   @Override
   public void stop() {
      this.turtle.goingHome = false;
   }

   @Override
   public boolean canContinueToUse() {
      return !this.turtle.homePos.closerToCenterThan(this.turtle.position(), 7.0) && !this.stuck && this.closeToHomeTryTicks <= this.adjustedTickDelay(600);
   }

   @Override
   public void tick() {
      BlockPos homePos = this.turtle.homePos;
      boolean closeToHome = homePos.closerToCenterThan(this.turtle.position(), 16.0);
      if (closeToHome) {
         this.closeToHomeTryTicks++;
      }

      if (this.turtle.getNavigation().isDone()) {
         Vec3 homePosVec = Vec3.atBottomCenterOf(homePos);
         Vec3 nextPos = DefaultRandomPos.getPosTowards(this.turtle, 16, 3, homePosVec, (float) (Math.PI / 10));
         if (nextPos == null) {
            nextPos = DefaultRandomPos.getPosTowards(this.turtle, 8, 7, homePosVec, (float) (Math.PI / 2));
         }

         if (nextPos != null && !closeToHome && !this.turtle.level().getBlockState(BlockPos.containing(nextPos)).is(Blocks.WATER)) {
            nextPos = DefaultRandomPos.getPosTowards(this.turtle, 16, 5, homePosVec, (float) (Math.PI / 2));
         }

         if (nextPos == null) {
            this.stuck = true;
            return;
         }

         this.turtle.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
      }
   }
}
