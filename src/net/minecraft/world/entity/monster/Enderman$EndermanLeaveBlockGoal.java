package net.minecraft.world.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

class Enderman$EndermanLeaveBlockGoal extends Goal {
   private final Enderman enderman;

   public Enderman$EndermanLeaveBlockGoal(final Enderman enderman) {
      this.enderman = enderman;
   }

   @Override
   public boolean canUse() {
      if (this.enderman.getCarriedBlock() == null) {
         return false;
      } else {
         return !getServerLevel(this.enderman).getGameRules().get(GameRules.MOB_GRIEFING)
            ? false
            : this.enderman.getRandom().nextInt(reducedTickDelay(2000)) == 0;
      }
   }

   @Override
   public void tick() {
      RandomSource random = this.enderman.getRandom();
      Level level = this.enderman.level();
      int xt = Mth.floor(this.enderman.getX() - 1.0 + random.nextDouble() * 2.0);
      int yt = Mth.floor(this.enderman.getY() + random.nextDouble() * 2.0);
      int zt = Mth.floor(this.enderman.getZ() - 1.0 + random.nextDouble() * 2.0);
      BlockPos pos = new BlockPos(xt, yt, zt);
      BlockState targetState = level.getBlockState(pos);
      BlockPos below = pos.below();
      BlockState belowState = level.getBlockState(below);
      BlockState carried = this.enderman.getCarriedBlock();
      if (carried != null) {
         carried = Block.updateFromNeighbourShapes(carried, this.enderman.level(), pos);
         if (this.canPlaceBlock(level, pos, carried, targetState, belowState, below)) {
            level.setBlockAndUpdate(pos, carried);
            level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent$Context.of(this.enderman, carried));
            this.enderman.setCarriedBlock(null);
         }
      }
   }

   private boolean canPlaceBlock(
      final Level level, final BlockPos pos, final BlockState carried, final BlockState targetState, final BlockState belowState, final BlockPos below
   ) {
      return targetState.isAir()
         && !belowState.isAir()
         && !belowState.is(Blocks.BEDROCK)
         && belowState.isCollisionShapeFullBlock(level, below)
         && carried.canSurvive(level, pos)
         && level.getEntities(this.enderman, AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(pos))).isEmpty();
   }
}
