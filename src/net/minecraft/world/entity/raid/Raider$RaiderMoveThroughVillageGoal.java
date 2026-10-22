package net.minecraft.world.entity.raid;

import com.google.common.collect.Lists;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager$Occupancy;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.phys.Vec3;

class Raider$RaiderMoveThroughVillageGoal extends Goal {
   private final Raider raider;
   private final double speedModifier;
   private BlockPos poiPos;
   private final List<BlockPos> visited = Lists.newArrayList();
   private final int distanceToPoi;
   private boolean stuck;

   public Raider$RaiderMoveThroughVillageGoal(final Raider mob, final double speedModifier, final int distanceToPoi) {
      this.raider = mob;
      this.speedModifier = speedModifier;
      this.distanceToPoi = distanceToPoi;
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      this.updateVisited();
      return this.isValidRaid() && this.hasSuitablePoi() && this.raider.getTarget() == null;
   }

   private boolean isValidRaid() {
      return this.raider.hasActiveRaid() && !this.raider.getCurrentRaid().isOver();
   }

   private boolean hasSuitablePoi() {
      ServerLevel level = (ServerLevel)this.raider.level();
      BlockPos pos = this.raider.blockPosition();
      Optional<BlockPos> homePos = level.getPoiManager()
         .getRandom(p -> p.is(PoiTypes.HOME), this::hasNotVisited, PoiManager$Occupancy.ANY, pos, 48, Raider.access$400(this.raider));
      if (homePos.isEmpty()) {
         return false;
      } else {
         this.poiPos = homePos.get().immutable();
         return true;
      }
   }

   @Override
   public boolean canContinueToUse() {
      return this.raider.getNavigation().isDone()
         ? false
         : this.raider.getTarget() == null
            && !this.poiPos.closerToCenterThan(this.raider.position(), this.raider.getBbWidth() + this.distanceToPoi)
            && !this.stuck;
   }

   @Override
   public void stop() {
      if (this.poiPos.closerToCenterThan(this.raider.position(), this.distanceToPoi)) {
         this.visited.add(this.poiPos);
      }
   }

   @Override
   public void start() {
      super.start();
      this.raider.setNoActionTime(0);
      this.raider.getNavigation().moveTo(this.poiPos.getX(), this.poiPos.getY(), this.poiPos.getZ(), this.speedModifier);
      this.stuck = false;
   }

   @Override
   public void tick() {
      if (this.raider.getNavigation().isDone()) {
         Vec3 poiVec = Vec3.atBottomCenterOf(this.poiPos);
         Vec3 nextPos = DefaultRandomPos.getPosTowards(this.raider, 16, 7, poiVec, (float) (Math.PI / 10));
         if (nextPos == null) {
            nextPos = DefaultRandomPos.getPosTowards(this.raider, 8, 7, poiVec, (float) (Math.PI / 2));
         }

         if (nextPos == null) {
            this.stuck = true;
            return;
         }

         this.raider.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
      }
   }

   private boolean hasNotVisited(final BlockPos poi) {
      for (BlockPos visitedPoi : this.visited) {
         if (Objects.equals(poi, visitedPoi)) {
            return false;
         }
      }

      return true;
   }

   private void updateVisited() {
      if (this.visited.size() > 2) {
         this.visited.remove(0);
      }
   }
}
