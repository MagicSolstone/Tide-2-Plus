package net.minecraft.world.entity.monster.warden;

import java.util.Objects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

class Warden$1 extends GroundPathNavigation {
   Warden$1(final Warden this$0, final Mob mob, final Level level) {
      Objects.requireNonNull(this$0);
      super(mob, level);
   }

   @Override
   protected PathFinder createPathFinder(final int maxVisitedNodes) {
      this.nodeEvaluator = new WalkNodeEvaluator();
      return new Warden$1$1(this, this.nodeEvaluator, maxVisitedNodes);
   }
}
