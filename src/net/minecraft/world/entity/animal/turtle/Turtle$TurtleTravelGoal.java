package net.minecraft.world.entity.animal.turtle;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

class Turtle$TurtleTravelGoal extends Goal {
   private final Turtle turtle;
   private final double speedModifier;
   private boolean stuck;

   public Turtle$TurtleTravelGoal(final Turtle turtle, final double speedModifier) {
      this.turtle = turtle;
      this.speedModifier = speedModifier;
   }

   @Override
   public boolean canUse() {
      return !this.turtle.goingHome && !this.turtle.hasEgg() && this.turtle.isInWater();
   }

   @Override
   public void start() {
      int xzDist = 512;
      int yDist = 4;
      RandomSource random = Turtle.access$000(this.turtle);
      int xt = random.nextInt(1025) - 512;
      int yt = random.nextInt(9) - 4;
      int zt = random.nextInt(1025) - 512;
      if (yt + this.turtle.getY() > this.turtle.level().getSeaLevel() - 1) {
         yt = 0;
      }

      this.turtle.travelPos = BlockPos.containing(xt + this.turtle.getX(), yt + this.turtle.getY(), zt + this.turtle.getZ());
      this.stuck = false;
   }

   @Override
   public void tick() {
      if (this.turtle.travelPos == null) {
         this.stuck = true;
      } else {
         if (this.turtle.getNavigation().isDone()) {
            Vec3 targetPos = Vec3.atBottomCenterOf(this.turtle.travelPos);
            Vec3 nextPos = DefaultRandomPos.getPosTowards(this.turtle, 16, 3, targetPos, (float) (Math.PI / 10));
            if (nextPos == null) {
               nextPos = DefaultRandomPos.getPosTowards(this.turtle, 8, 7, targetPos, (float) (Math.PI / 2));
            }

            if (nextPos != null) {
               int xc = Mth.floor(nextPos.x);
               int zc = Mth.floor(nextPos.z);
               int r = 34;
               if (!this.turtle.level().hasChunksAt(xc - 34, zc - 34, xc + 34, zc + 34)) {
                  nextPos = null;
               }
            }

            if (nextPos == null) {
               this.stuck = true;
               return;
            }

            this.turtle.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
         }
      }
   }

   @Override
   public boolean canContinueToUse() {
      return !this.turtle.getNavigation().isDone() && !this.stuck && !this.turtle.goingHome && !this.turtle.isInLove() && !this.turtle.hasEgg();
   }

   @Override
   public void stop() {
      this.turtle.travelPos = null;
      super.stop();
   }
}
