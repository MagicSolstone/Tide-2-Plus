package net.minecraft.world.entity.animal.turtle;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;

class Turtle$TurtleGoToWaterGoal extends MoveToBlockGoal {
   private static final int GIVE_UP_TICKS = 1200;
   private final Turtle turtle;

   private Turtle$TurtleGoToWaterGoal(final Turtle turtle, final double speedModifier) {
      super(turtle, turtle.isBaby() ? 2.0 : speedModifier, 24);
      this.turtle = turtle;
      this.verticalSearchStart = -1;
   }

   @Override
   public boolean canContinueToUse() {
      return !this.turtle.isInWater() && this.tryTicks <= 1200 && this.isValidTarget(this.turtle.level(), this.blockPos);
   }

   @Override
   public boolean canUse() {
      if (this.turtle.isBaby() && !this.turtle.isInWater()) {
         return super.canUse();
      } else {
         return !this.turtle.goingHome && !this.turtle.isInWater() && !this.turtle.hasEgg() ? super.canUse() : false;
      }
   }

   @Override
   public boolean shouldRecalculatePath() {
      return this.tryTicks % 160 == 0;
   }

   @Override
   protected boolean isValidTarget(final LevelReader level, final BlockPos pos) {
      return level.getBlockState(pos).is(Blocks.WATER);
   }
}
