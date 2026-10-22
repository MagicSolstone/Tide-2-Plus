package net.minecraft.world.entity.monster.warden;

import java.util.Objects;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;

class Warden$1$1 extends PathFinder {
   Warden$1$1(final Warden$1 this$1, final NodeEvaluator nodeEvaluator, final int maxVisitedNodes) {
      Objects.requireNonNull(this$1);
      super(nodeEvaluator, maxVisitedNodes);
   }

   @Override
   protected float distance(final Node from, final Node to) {
      return from.distanceToXZ(to);
   }
}
