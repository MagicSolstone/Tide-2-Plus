package net.minecraft.world.entity.animal.rabbit;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarrotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.level.gamerules.GameRules;

class Rabbit$RaidGardenGoal extends MoveToBlockGoal {
   private final Rabbit rabbit;
   private boolean wantsToRaid;
   private boolean canRaid;

   public Rabbit$RaidGardenGoal(final Rabbit rabbit) {
      super(rabbit, 0.7F, 16);
      this.rabbit = rabbit;
   }

   @Override
   public boolean canUse() {
      if (this.nextStartTick <= 0) {
         if (!getServerLevel(this.rabbit).getGameRules().get(GameRules.MOB_GRIEFING)) {
            return false;
         }

         this.canRaid = false;
         this.wantsToRaid = this.rabbit.wantsMoreFood();
      }

      return super.canUse();
   }

   @Override
   public boolean canContinueToUse() {
      return this.canRaid && super.canContinueToUse();
   }

   @Override
   public void tick() {
      super.tick();
      this.rabbit
         .getLookControl()
         .setLookAt(this.blockPos.getX() + 0.5, this.blockPos.getY() + 1, this.blockPos.getZ() + 0.5, 10.0F, this.rabbit.getMaxHeadXRot());
      if (this.isReachedTarget()) {
         Level level = this.rabbit.level();
         BlockPos cropsPos = this.blockPos.above();
         BlockState blockState = level.getBlockState(cropsPos);
         Block block = blockState.getBlock();
         if (this.canRaid && block instanceof CarrotBlock) {
            int carrotAge = blockState.getValue(CarrotBlock.AGE);
            if (carrotAge == 0) {
               level.setBlock(cropsPos, Blocks.AIR.defaultBlockState(), 2);
               level.destroyBlock(cropsPos, true, this.rabbit);
            } else {
               level.setBlock(cropsPos, blockState.setValue(CarrotBlock.AGE, carrotAge - 1), 2);
               level.gameEvent(GameEvent.BLOCK_CHANGE, cropsPos, GameEvent$Context.of(this.rabbit));
               level.levelEvent(2001, cropsPos, Block.getId(blockState));
            }

            this.rabbit.moreCarrotTicks = 40;
         }

         this.canRaid = false;
         this.nextStartTick = 10;
      }
   }

   @Override
   protected boolean isValidTarget(final LevelReader level, final BlockPos pos) {
      BlockState state = level.getBlockState(pos);
      if (state.is(BlockTags.SUPPORTS_CROPS) && this.wantsToRaid && !this.canRaid) {
         state = level.getBlockState(pos.above());
         if (state.getBlock() instanceof CarrotBlock carrotBlock && carrotBlock.isMaxAge(state)) {
            this.canRaid = true;
            return true;
         }
      }

      return false;
   }
}
