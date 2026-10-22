package net.minecraft.world.entity.monster.creaking;

import java.util.Objects;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;

class Creaking$CreakingPathNavigation extends GroundPathNavigation {
   public Creaking$CreakingPathNavigation(final Creaking this$0, final Creaking mob, final Level level) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(mob, level);
   }

   @Override
   public void tick() {
      if (this.this$0.canMove()) {
         super.tick();
      }
   }

   @Override
   protected PathFinder createPathFinder(final int maxVisitedNodes) {
      this.nodeEvaluator = new Creaking$HomeNodeEvaluator(this.this$0);
      this.nodeEvaluator.setCanPassDoors(true);
      return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
   }
}
