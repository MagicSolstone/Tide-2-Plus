package net.minecraft.world.entity.monster.creaking;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

class Creaking$HomeNodeEvaluator extends WalkNodeEvaluator {
   private static final int MAX_DISTANCE_TO_HOME_SQ = 1024;

   private Creaking$HomeNodeEvaluator(final Creaking this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
   }

   @Override
   public PathType getPathType(final PathfindingContext context, final int x, final int y, final int z) {
      BlockPos homePos = this.this$0.getHomePos();
      if (homePos == null) {
         return super.getPathType(context, x, y, z);
      } else {
         double homeDistance = homePos.distSqr(new Vec3i(x, y, z));
         return homeDistance > 1024.0 && homeDistance >= homePos.distSqr(context.mobPosition()) ? PathType.BLOCKED : super.getPathType(context, x, y, z);
      }
   }
}
