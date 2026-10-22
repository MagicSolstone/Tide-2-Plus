package net.minecraft.world.entity.animal.bee;

import com.google.common.collect.Lists;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.level.pathfinder.Path;
import org.jspecify.annotations.Nullable;

@VisibleForDebug
public class Bee$BeeGoToHiveGoal extends Bee$BaseBeeGoal {
   public static final int MAX_TRAVELLING_TICKS = 2400;
   private int travellingTicks;
   private static final int MAX_BLACKLISTED_TARGETS = 3;
   private final List<BlockPos> blacklistedTargets;
   @Nullable
   private Path lastPath;
   private static final int TICKS_BEFORE_HIVE_DROP = 60;
   private int ticksStuck;

   public Bee$BeeGoToHiveGoal(final Bee this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(this$0);
      this.blacklistedTargets = Lists.newArrayList();
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canBeeUse() {
      return this.this$0.hivePos != null
         && !this.this$0.isTooFarAway(this.this$0.hivePos)
         && !this.this$0.hasHome()
         && this.this$0.wantsToEnterHive()
         && !this.hasReachedTarget(this.this$0.hivePos)
         && this.this$0.level().getBlockState(this.this$0.hivePos).is(BlockTags.BEEHIVES);
   }

   @Override
   public boolean canBeeContinueToUse() {
      return this.canBeeUse();
   }

   @Override
   public void start() {
      this.travellingTicks = 0;
      this.ticksStuck = 0;
      super.start();
   }

   @Override
   public void stop() {
      this.travellingTicks = 0;
      this.ticksStuck = 0;
      Bee.access$500(this.this$0).stop();
      Bee.access$600(this.this$0).resetMaxVisitedNodesMultiplier();
   }

   @Override
   public void tick() {
      if (this.this$0.hivePos != null) {
         this.travellingTicks++;
         if (this.travellingTicks > this.adjustedTickDelay(2400)) {
            this.dropAndBlacklistHive();
         } else if (!Bee.access$700(this.this$0).isInProgress()) {
            if (!this.this$0.closerThan(this.this$0.hivePos, 16)) {
               if (this.this$0.isTooFarAway(this.this$0.hivePos)) {
                  this.this$0.dropHive();
               } else {
                  this.this$0.pathfindRandomlyTowards(this.this$0.hivePos);
               }
            } else {
               boolean canReachAllTheWayToTarget = this.pathfindDirectlyTowards(this.this$0.hivePos);
               if (!canReachAllTheWayToTarget) {
                  this.dropAndBlacklistHive();
               } else if (this.lastPath != null && Bee.access$800(this.this$0).getPath().sameAs(this.lastPath)) {
                  this.ticksStuck++;
                  if (this.ticksStuck > 60) {
                     this.this$0.dropHive();
                     this.ticksStuck = 0;
                  }
               } else {
                  this.lastPath = Bee.access$900(this.this$0).getPath();
               }
            }
         }
      }
   }

   private boolean pathfindDirectlyTowards(final BlockPos targetPos) {
      int closeEnough = this.this$0.closerThan(targetPos, 3) ? 1 : 2;
      Bee.access$1000(this.this$0).setMaxVisitedNodesMultiplier(10.0F);
      Bee.access$1100(this.this$0).moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), closeEnough, 1.0);
      return Bee.access$1200(this.this$0).getPath() != null && Bee.access$1300(this.this$0).getPath().canReach();
   }

   private boolean isTargetBlacklisted(final BlockPos targetPos) {
      return this.blacklistedTargets.contains(targetPos);
   }

   private void blacklistTarget(final BlockPos targetPos) {
      this.blacklistedTargets.add(targetPos);

      while (this.blacklistedTargets.size() > 3) {
         this.blacklistedTargets.remove(0);
      }
   }

   private void clearBlacklist() {
      this.blacklistedTargets.clear();
   }

   private void dropAndBlacklistHive() {
      if (this.this$0.hivePos != null) {
         this.blacklistTarget(this.this$0.hivePos);
      }

      this.this$0.dropHive();
   }

   private boolean hasReachedTarget(final BlockPos targetPos) {
      if (this.this$0.closerThan(targetPos, 2)) {
         return true;
      } else {
         Path path = Bee.access$1400(this.this$0).getPath();
         return path != null && path.getTarget().equals(targetPos) && path.canReach() && path.isDone();
      }
   }
}
