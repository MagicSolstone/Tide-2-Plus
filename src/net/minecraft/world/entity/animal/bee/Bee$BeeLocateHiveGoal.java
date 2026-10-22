package net.minecraft.world.entity.animal.bee;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiManager$Occupancy;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;

class Bee$BeeLocateHiveGoal extends Bee$BaseBeeGoal {
   private Bee$BeeLocateHiveGoal(final Bee this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public boolean canBeeUse() {
      return this.this$0.remainingCooldownBeforeLocatingNewHive == 0 && !this.this$0.hasHive() && this.this$0.wantsToEnterHive();
   }

   @Override
   public boolean canBeeContinueToUse() {
      return false;
   }

   @Override
   public void start() {
      this.this$0.remainingCooldownBeforeLocatingNewHive = 200;
      List<BlockPos> hivesWithSpace = this.findNearbyHivesWithSpace();
      if (!hivesWithSpace.isEmpty()) {
         for (BlockPos posToCheck : hivesWithSpace) {
            if (!this.this$0.goToHiveGoal.isTargetBlacklisted(posToCheck)) {
               this.this$0.hivePos = posToCheck;
               return;
            }
         }

         this.this$0.goToHiveGoal.clearBlacklist();
         this.this$0.hivePos = hivesWithSpace.get(0);
      }
   }

   private List<BlockPos> findNearbyHivesWithSpace() {
      BlockPos beePos = this.this$0.blockPosition();
      PoiManager poiManager = ((ServerLevel)this.this$0.level()).getPoiManager();
      Stream<PoiRecord> nearbyHives = poiManager.getInRange(p -> p.is(PoiTypeTags.BEE_HOME), beePos, 20, PoiManager$Occupancy.ANY);
      return nearbyHives.map(PoiRecord::getPos)
         .filter(this.this$0::doesHiveHaveSpace)
         .sorted(Comparator.comparingDouble(pos -> pos.distSqr(beePos)))
         .collect(Collectors.toList());
   }
}
