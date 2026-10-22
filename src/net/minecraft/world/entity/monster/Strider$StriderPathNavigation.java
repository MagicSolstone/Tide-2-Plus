package net.minecraft.world.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

class Strider$StriderPathNavigation extends GroundPathNavigation {
   public Strider$StriderPathNavigation(final Strider mob, final Level level) {
      super(mob, level);
   }

   @Override
   protected PathFinder createPathFinder(final int maxVisitedNodes) {
      this.nodeEvaluator = new WalkNodeEvaluator();
      return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
   }

   @Override
   public boolean isStableDestination(final BlockPos pos) {
      return this.level.getBlockState(pos).is(Blocks.LAVA) || super.isStableDestination(pos);
   }
}
