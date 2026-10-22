package net.minecraft.world.entity.monster.zombie;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;

class Drowned$DrownedGoToBeachGoal extends MoveToBlockGoal {
   private final Drowned drowned;

   public Drowned$DrownedGoToBeachGoal(final Drowned drowned, final double speedModifier) {
      super(drowned, speedModifier, 8, 2);
      this.drowned = drowned;
   }

   @Override
   public boolean canUse() {
      return super.canUse()
         && !this.drowned.level().isBrightOutside()
         && this.drowned.isInWater()
         && this.drowned.getY() >= this.drowned.level().getSeaLevel() - 3;
   }

   @Override
   public boolean canContinueToUse() {
      return super.canContinueToUse();
   }

   @Override
   protected boolean isValidTarget(final LevelReader level, final BlockPos pos) {
      BlockPos above = pos.above();
      return level.isEmptyBlock(above) && level.isEmptyBlock(above.above()) ? level.getBlockState(pos).entityCanStandOn(level, pos, this.drowned) : false;
   }

   @Override
   public void start() {
      this.drowned.setSearchingForLand(false);
      super.start();
   }

   @Override
   public void stop() {
      super.stop();
   }
}
