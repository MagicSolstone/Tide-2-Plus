package net.minecraft.world.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;

class Silverfish$SilverfishWakeUpFriendsGoal extends Goal {
   private final Silverfish silverfish;
   private int lookForFriends;

   public Silverfish$SilverfishWakeUpFriendsGoal(final Silverfish silverfish) {
      this.silverfish = silverfish;
   }

   public void notifyHurt() {
      if (this.lookForFriends == 0) {
         this.lookForFriends = this.adjustedTickDelay(20);
      }
   }

   @Override
   public boolean canUse() {
      return this.lookForFriends > 0;
   }

   @Override
   public void tick() {
      this.lookForFriends--;
      if (this.lookForFriends <= 0) {
         Level level = this.silverfish.level();
         RandomSource random = this.silverfish.getRandom();
         BlockPos basePos = this.silverfish.blockPosition();

         for (int yOff = 0; yOff <= 5 && yOff >= -5; yOff = (yOff <= 0 ? 1 : 0) - yOff) {
            for (int xOff = 0; xOff <= 10 && xOff >= -10; xOff = (xOff <= 0 ? 1 : 0) - xOff) {
               for (int zOff = 0; zOff <= 10 && zOff >= -10; zOff = (zOff <= 0 ? 1 : 0) - zOff) {
                  BlockPos testPos = basePos.offset(xOff, yOff, zOff);
                  BlockState blockState = level.getBlockState(testPos);
                  if (blockState.getBlock() instanceof InfestedBlock infestedBlock) {
                     if (getServerLevel(level).getGameRules().get(GameRules.MOB_GRIEFING)) {
                        level.destroyBlock(testPos, true, this.silverfish);
                     } else {
                        level.setBlockAndUpdate(testPos, infestedBlock.hostStateByInfested(level.getBlockState(testPos)));
                     }

                     if (random.nextBoolean()) {
                        return;
                     }
                  }
               }
            }
         }
      }
   }
}
