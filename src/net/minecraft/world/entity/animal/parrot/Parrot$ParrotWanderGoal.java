package net.minecraft.world.entity.animal.parrot;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

class Parrot$ParrotWanderGoal extends WaterAvoidingRandomFlyingGoal {
   public Parrot$ParrotWanderGoal(final PathfinderMob mob, final double speedModifier) {
      super(mob, speedModifier);
   }

   @Nullable
   @Override
   protected Vec3 getPosition() {
      Vec3 pos = null;
      if (this.mob.isInWater()) {
         pos = LandRandomPos.getPos(this.mob, 15, 15);
      }

      if (this.mob.getRandom().nextFloat() >= this.probability) {
         pos = this.getTreePos();
      }

      return pos == null ? super.getPosition() : pos;
   }

   @Nullable
   private Vec3 getTreePos() {
      BlockPos mobPos = this.mob.blockPosition();
      BlockPos$MutableBlockPos abovePos = new BlockPos$MutableBlockPos();
      BlockPos$MutableBlockPos belowPos = new BlockPos$MutableBlockPos();

      for (BlockPos pos : BlockPos.betweenClosed(
         Mth.floor(this.mob.getX() - 3.0),
         Mth.floor(this.mob.getY() - 6.0),
         Mth.floor(this.mob.getZ() - 3.0),
         Mth.floor(this.mob.getX() + 3.0),
         Mth.floor(this.mob.getY() + 6.0),
         Mth.floor(this.mob.getZ() + 3.0)
      )) {
         if (!mobPos.equals(pos)) {
            BlockState state = this.mob.level().getBlockState(belowPos.setWithOffset(pos, Direction.DOWN));
            boolean canSitOn = state.getBlock() instanceof LeavesBlock || state.is(BlockTags.LOGS);
            if (canSitOn && this.mob.level().isEmptyBlock(pos) && this.mob.level().isEmptyBlock(abovePos.setWithOffset(pos, Direction.UP))) {
               return Vec3.atBottomCenterOf(pos);
            }
         }
      }

      return null;
   }
}
