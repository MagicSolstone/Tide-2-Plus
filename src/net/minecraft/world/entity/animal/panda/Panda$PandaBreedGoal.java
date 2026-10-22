package net.minecraft.world.entity.animal.panda;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

class Panda$PandaBreedGoal extends BreedGoal {
   private final Panda panda;
   private int unhappyCooldown;

   public Panda$PandaBreedGoal(final Panda panda, final double speedModifier) {
      super(panda, speedModifier);
      this.panda = panda;
   }

   @Override
   public boolean canUse() {
      if (!super.canUse() || this.panda.getUnhappyCounter() != 0) {
         return false;
      } else if (!this.canFindBamboo()) {
         if (this.unhappyCooldown <= this.panda.tickCount) {
            this.panda.setUnhappyCounter(32);
            this.unhappyCooldown = this.panda.tickCount + 600;
            if (this.panda.isEffectiveAi()) {
               Player player = this.level.getNearestPlayer(Panda.BREED_TARGETING, this.panda);
               this.panda.lookAtPlayerGoal.setTarget(player);
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private boolean canFindBamboo() {
      BlockPos pandaPos = this.panda.blockPosition();
      BlockPos$MutableBlockPos pos = new BlockPos$MutableBlockPos();

      for (int yOff = 0; yOff < 3; yOff++) {
         for (int r = 0; r < 8; r++) {
            for (int x = 0; x <= r; x = x > 0 ? -x : 1 - x) {
               for (int z = x < r && x > -r ? r : 0; z <= r; z = z > 0 ? -z : 1 - z) {
                  pos.setWithOffset(pandaPos, x, yOff, z);
                  if (this.level.getBlockState(pos).is(Blocks.BAMBOO)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }
}
