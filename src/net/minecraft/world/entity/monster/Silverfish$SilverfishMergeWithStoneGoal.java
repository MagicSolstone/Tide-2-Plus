package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import org.jspecify.annotations.Nullable;

class Silverfish$SilverfishMergeWithStoneGoal extends RandomStrollGoal {
   @Nullable
   private Direction selectedDirection;
   private boolean doMerge;

   public Silverfish$SilverfishMergeWithStoneGoal(final Silverfish silverfish) {
      super(silverfish, 1.0, 10);
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      if (this.mob.getTarget() != null) {
         return false;
      } else if (!this.mob.getNavigation().isDone()) {
         return false;
      } else {
         RandomSource random = this.mob.getRandom();
         if (getServerLevel(this.mob).getGameRules().get(GameRules.MOB_GRIEFING) && random.nextInt(reducedTickDelay(10)) == 0) {
            this.selectedDirection = Direction.getRandom(random);
            BlockPos pos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5, this.mob.getZ()).relative(this.selectedDirection);
            BlockState blockState = this.mob.level().getBlockState(pos);
            if (InfestedBlock.isCompatibleHostBlock(blockState)) {
               this.doMerge = true;
               return true;
            }
         }

         this.doMerge = false;
         return super.canUse();
      }
   }

   @Override
   public boolean canContinueToUse() {
      return this.doMerge ? false : super.canContinueToUse();
   }

   @Override
   public void start() {
      if (!this.doMerge) {
         super.start();
      } else {
         LevelAccessor level = this.mob.level();
         BlockPos pos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5, this.mob.getZ()).relative(this.selectedDirection);
         BlockState blockState = level.getBlockState(pos);
         if (InfestedBlock.isCompatibleHostBlock(blockState)) {
            level.setBlockAndUpdate(pos, InfestedBlock.infestedStateByHost(blockState));
            this.mob.spawnAnim();
            this.mob.discard();
         }
      }
   }
}
