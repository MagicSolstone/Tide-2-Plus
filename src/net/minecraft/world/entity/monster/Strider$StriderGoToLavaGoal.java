package net.minecraft.world.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathComputationType;

class Strider$StriderGoToLavaGoal extends MoveToBlockGoal {
   private final Strider strider;

   private Strider$StriderGoToLavaGoal(final Strider strider, final double speedModifier) {
      super(strider, speedModifier, 8, 2);
      this.strider = strider;
   }

   @Override
   public BlockPos getMoveToTarget() {
      return this.blockPos;
   }

   @Override
   public boolean canContinueToUse() {
      return !this.strider.isInLava() && this.isValidTarget(this.strider.level(), this.blockPos);
   }

   @Override
   public boolean canUse() {
      return !this.strider.isInLava() && super.canUse();
   }

   @Override
   public boolean shouldRecalculatePath() {
      return this.tryTicks % 20 == 0;
   }

   @Override
   protected boolean isValidTarget(final LevelReader level, final BlockPos pos) {
      return level.getBlockState(pos).is(Blocks.LAVA) && level.getBlockState(pos.above()).isPathfindable(PathComputationType.LAND);
   }
}
