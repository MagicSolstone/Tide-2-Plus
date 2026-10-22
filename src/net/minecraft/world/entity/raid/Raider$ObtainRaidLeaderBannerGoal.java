package net.minecraft.world.entity.raid;

import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.Path;
import org.jspecify.annotations.Nullable;

public class Raider$ObtainRaidLeaderBannerGoal<T extends Raider> extends Goal {
   private final T mob;
   private Int2LongOpenHashMap unreachableBannerCache;
   @Nullable
   private Path pathToBanner;
   @Nullable
   private ItemEntity pursuedBannerItemEntity;

   public Raider$ObtainRaidLeaderBannerGoal(final T this$0, final Raider mob) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.unreachableBannerCache = new Int2LongOpenHashMap();
      this.mob = mob;
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      if (this.cannotPickUpBanner()) {
         return false;
      } else {
         Int2LongOpenHashMap tempCache = new Int2LongOpenHashMap();
         double followRange = this.this$0.getAttributeValue(Attributes.FOLLOW_RANGE);

         for (ItemEntity banner : this.mob
            .level()
            .getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(followRange, 8.0, followRange), Raider.ALLOWED_ITEMS)) {
            long unreachableUntilTime = this.unreachableBannerCache.getOrDefault(banner.getId(), Long.MIN_VALUE);
            if (this.this$0.level().getGameTime() < unreachableUntilTime) {
               tempCache.put(banner.getId(), unreachableUntilTime);
            } else {
               Path path = this.mob.getNavigation().createPath(banner, 1);
               if (path != null && path.canReach()) {
                  this.pathToBanner = path;
                  this.pursuedBannerItemEntity = banner;
                  return true;
               }

               tempCache.put(banner.getId(), this.this$0.level().getGameTime() + 600L);
            }
         }

         this.unreachableBannerCache = tempCache;
         return false;
      }
   }

   @Override
   public boolean canContinueToUse() {
      if (this.pursuedBannerItemEntity == null || this.pathToBanner == null) {
         return false;
      } else if (this.pursuedBannerItemEntity.isRemoved()) {
         return false;
      } else {
         return this.pathToBanner.isDone() ? false : !this.cannotPickUpBanner();
      }
   }

   private boolean cannotPickUpBanner() {
      if (!this.mob.hasActiveRaid()) {
         return true;
      } else if (this.mob.getCurrentRaid().isOver()) {
         return true;
      } else if (!this.mob.canBeLeader()) {
         return true;
      } else if (ItemStack.matches(
         this.mob.getItemBySlot(EquipmentSlot.HEAD), Raid.getOminousBannerInstance(this.mob.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN))
      )) {
         return true;
      } else {
         Raider leader = this.this$0.raid.getLeader(this.mob.getWave());
         return leader != null && leader.isAlive();
      }
   }

   @Override
   public void start() {
      this.mob.getNavigation().moveTo(this.pathToBanner, 1.15F);
   }

   @Override
   public void stop() {
      this.pathToBanner = null;
      this.pursuedBannerItemEntity = null;
   }

   @Override
   public void tick() {
      if (this.pursuedBannerItemEntity != null && this.pursuedBannerItemEntity.closerThan(this.mob, 1.414)) {
         this.mob.pickUpItem(getServerLevel(this.this$0.level()), this.pursuedBannerItemEntity);
      }
   }
}
