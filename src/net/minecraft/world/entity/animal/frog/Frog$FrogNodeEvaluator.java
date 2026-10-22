package net.minecraft.world.entity.animal.frog;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;

class Frog$FrogNodeEvaluator extends AmphibiousNodeEvaluator {
   private final BlockPos$MutableBlockPos belowPos = new BlockPos$MutableBlockPos();

   public Frog$FrogNodeEvaluator(final boolean prefersShallowSwimming) {
      super(prefersShallowSwimming);
   }

   @Override
   public Node getStart() {
      return !this.mob.isInWater()
         ? super.getStart()
         : this.getStartNode(
            new BlockPos(Mth.floor(this.mob.getBoundingBox().minX), Mth.floor(this.mob.getBoundingBox().minY), Mth.floor(this.mob.getBoundingBox().minZ))
         );
   }

   @Override
   public PathType getPathType(final PathfindingContext context, final int x, final int y, final int z) {
      this.belowPos.set(x, y - 1, z);
      BlockState belowState = context.getBlockState(this.belowPos);
      return belowState.is(BlockTags.FROG_PREFER_JUMP_TO) ? PathType.OPEN : super.getPathType(context, x, y, z);
   }
}
