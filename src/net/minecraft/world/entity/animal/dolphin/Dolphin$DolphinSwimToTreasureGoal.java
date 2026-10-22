package net.minecraft.world.entity.animal.dolphin;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

class Dolphin$DolphinSwimToTreasureGoal extends Goal {
   private final Dolphin dolphin;
   private boolean stuck;

   public Dolphin$DolphinSwimToTreasureGoal(final Dolphin dolphin) {
      this.dolphin = dolphin;
      this.setFlags(EnumSet.of(Goal$Flag.MOVE, Goal$Flag.LOOK));
   }

   @Override
   public boolean isInterruptable() {
      return false;
   }

   @Override
   public boolean canUse() {
      return this.dolphin.gotFish() && this.dolphin.getAirSupply() >= 100;
   }

   @Override
   public boolean canContinueToUse() {
      BlockPos treasurePos = this.dolphin.treasurePos;
      return treasurePos == null
         ? false
         : !BlockPos.containing(treasurePos.getX(), this.dolphin.getY(), treasurePos.getZ()).closerToCenterThan(this.dolphin.position(), 4.0)
            && !this.stuck
            && this.dolphin.getAirSupply() >= 100;
   }

   @Override
   public void start() {
      if (this.dolphin.level() instanceof ServerLevel) {
         ServerLevel level = (ServerLevel)this.dolphin.level();
         this.stuck = false;
         this.dolphin.getNavigation().stop();
         BlockPos dolphinPos = this.dolphin.blockPosition();
         BlockPos treasurePos = level.findNearestMapStructure(StructureTags.DOLPHIN_LOCATED, dolphinPos, 50, false);
         if (treasurePos != null) {
            this.dolphin.treasurePos = treasurePos;
            level.broadcastEntityEvent(this.dolphin, (byte)38);
         } else {
            this.stuck = true;
         }
      }
   }

   @Override
   public void stop() {
      BlockPos treasurePos = this.dolphin.treasurePos;
      if (treasurePos == null
         || BlockPos.containing(treasurePos.getX(), this.dolphin.getY(), treasurePos.getZ()).closerToCenterThan(this.dolphin.position(), 4.0)
         || this.stuck) {
         this.dolphin.setGotFish(false);
      }
   }

   @Override
   public void tick() {
      if (this.dolphin.treasurePos != null) {
         Level level = this.dolphin.level();
         if (this.dolphin.closeToNextPos() || this.dolphin.getNavigation().isDone()) {
            Vec3 treasurePos = Vec3.atCenterOf(this.dolphin.treasurePos);
            Vec3 nextPos = DefaultRandomPos.getPosTowards(this.dolphin, 16, 1, treasurePos, (float) (Math.PI / 8));
            if (nextPos == null) {
               nextPos = DefaultRandomPos.getPosTowards(this.dolphin, 8, 4, treasurePos, (float) (Math.PI / 2));
            }

            if (nextPos != null) {
               BlockPos next = BlockPos.containing(nextPos);
               if (!level.getFluidState(next).is(FluidTags.WATER) || !level.getBlockState(next).isPathfindable(PathComputationType.WATER)) {
                  nextPos = DefaultRandomPos.getPosTowards(this.dolphin, 8, 5, treasurePos, (float) (Math.PI / 2));
               }
            }

            if (nextPos == null) {
               this.stuck = true;
               return;
            }

            this.dolphin.getLookControl().setLookAt(nextPos.x, nextPos.y, nextPos.z, this.dolphin.getMaxHeadYRot() + 20, this.dolphin.getMaxHeadXRot());
            this.dolphin.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, 1.3);
            if (level.getRandom().nextInt(this.adjustedTickDelay(80)) == 0) {
               level.broadcastEntityEvent(this.dolphin, (byte)38);
            }
         }
      }
   }
}
