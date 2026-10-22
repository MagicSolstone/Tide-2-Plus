package net.minecraft.world.entity.animal.frog;

import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;

class Frog$FrogPathNavigation extends AmphibiousPathNavigation {
   public Frog$FrogPathNavigation(final Frog mob, final Level level) {
      super(mob, level);
   }

   @Override
   public boolean canCutCorner(final PathType pathType) {
      return pathType != PathType.WATER_BORDER && super.canCutCorner(pathType);
   }

   @Override
   protected PathFinder createPathFinder(final int maxVisitedNodes) {
      this.nodeEvaluator = new Frog$FrogNodeEvaluator(true);
      return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
   }
}
