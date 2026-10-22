package net.minecraft.world.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext$Block;
import net.minecraft.world.level.ClipContext$Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

class Enderman$EndermanTakeBlockGoal extends Goal {
   private final Enderman enderman;

   public Enderman$EndermanTakeBlockGoal(final Enderman enderman) {
      this.enderman = enderman;
   }

   @Override
   public boolean canUse() {
      if (this.enderman.getCarriedBlock() != null) {
         return false;
      } else {
         return !getServerLevel(this.enderman).getGameRules().get(GameRules.MOB_GRIEFING)
            ? false
            : this.enderman.getRandom().nextInt(reducedTickDelay(20)) == 0;
      }
   }

   @Override
   public void tick() {
      RandomSource random = this.enderman.getRandom();
      Level level = this.enderman.level();
      int xt = Mth.floor(this.enderman.getX() - 2.0 + random.nextDouble() * 4.0);
      int yt = Mth.floor(this.enderman.getY() + random.nextDouble() * 3.0);
      int zt = Mth.floor(this.enderman.getZ() - 2.0 + random.nextDouble() * 4.0);
      BlockPos pos = new BlockPos(xt, yt, zt);
      BlockState blockState = level.getBlockState(pos);
      Vec3 from = new Vec3(this.enderman.getBlockX() + 0.5, yt + 0.5, this.enderman.getBlockZ() + 0.5);
      Vec3 to = new Vec3(xt + 0.5, yt + 0.5, zt + 0.5);
      BlockHitResult result = level.clip(new ClipContext(from, to, ClipContext$Block.OUTLINE, ClipContext$Fluid.NONE, this.enderman));
      boolean reachable = result.getBlockPos().equals(pos);
      if (blockState.is(BlockTags.ENDERMAN_HOLDABLE) && reachable) {
         level.removeBlock(pos, false);
         level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent$Context.of(this.enderman, blockState));
         this.enderman.setCarriedBlock(blockState.getBlock().defaultBlockState());
      }
   }
}
