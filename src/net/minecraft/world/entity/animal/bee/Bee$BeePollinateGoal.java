package net.minecraft.world.entity.animal.bee;

import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

class Bee$BeePollinateGoal extends Bee$BaseBeeGoal {
   private static final int MIN_POLLINATION_TICKS = 400;
   private static final double ARRIVAL_THRESHOLD = 0.1;
   private static final int POSITION_CHANGE_CHANCE = 25;
   private static final float SPEED_MODIFIER = 0.35F;
   private static final float HOVER_HEIGHT_WITHIN_FLOWER = 0.6F;
   private static final float HOVER_POS_OFFSET = 0.33333334F;
   private static final int FLOWER_SEARCH_RADIUS = 5;
   private int successfulPollinatingTicks;
   private int lastSoundPlayedTick;
   private boolean pollinating;
   @Nullable
   private Vec3 hoverPos;
   private int pollinatingTicks;
   private static final int MAX_POLLINATING_TICKS = 600;
   private Long2LongOpenHashMap unreachableFlowerCache;

   public Bee$BeePollinateGoal(final Bee this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
      this.unreachableFlowerCache = new Long2LongOpenHashMap();
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canBeeUse() {
      if (this.this$0.remainingCooldownBeforeLocatingNewFlower > 0) {
         return false;
      } else if (this.this$0.hasNectar()) {
         return false;
      } else if (this.this$0.level().isRaining()) {
         return false;
      } else {
         Optional<BlockPos> nearbyPos = this.findNearbyFlower();
         if (nearbyPos.isPresent()) {
            this.this$0.savedFlowerPos = nearbyPos.get();
            Bee.access$1800(this.this$0)
               .moveTo(this.this$0.savedFlowerPos.getX() + 0.5, this.this$0.savedFlowerPos.getY() + 0.5, this.this$0.savedFlowerPos.getZ() + 0.5, 1.2F);
            return true;
         } else {
            this.this$0.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(Bee.access$1900(this.this$0), 20, 60);
            return false;
         }
      }
   }

   @Override
   public boolean canBeeContinueToUse() {
      if (!this.pollinating) {
         return false;
      } else if (!this.this$0.hasSavedFlowerPos()) {
         return false;
      } else if (this.this$0.level().isRaining()) {
         return false;
      } else {
         return this.hasPollinatedLongEnough() ? Bee.access$2000(this.this$0).nextFloat() < 0.2F : true;
      }
   }

   private boolean hasPollinatedLongEnough() {
      return this.successfulPollinatingTicks > 400;
   }

   private boolean isPollinating() {
      return this.pollinating;
   }

   private void stopPollinating() {
      this.pollinating = false;
   }

   @Override
   public void start() {
      this.successfulPollinatingTicks = 0;
      this.pollinatingTicks = 0;
      this.lastSoundPlayedTick = 0;
      this.pollinating = true;
      this.this$0.resetTicksWithoutNectarSinceExitingHive();
   }

   @Override
   public void stop() {
      if (this.hasPollinatedLongEnough()) {
         this.this$0.setHasNectar(true);
      }

      this.pollinating = false;
      Bee.access$2100(this.this$0).stop();
      this.this$0.remainingCooldownBeforeLocatingNewFlower = 200;
   }

   @Override
   public boolean requiresUpdateEveryTick() {
      return true;
   }

   @Override
   public void tick() {
      if (this.this$0.hasSavedFlowerPos()) {
         this.pollinatingTicks++;
         if (this.pollinatingTicks > 600) {
            this.this$0.dropFlower();
            this.pollinating = false;
            this.this$0.remainingCooldownBeforeLocatingNewFlower = 200;
         } else {
            Vec3 flowerPos = Vec3.atBottomCenterOf(this.this$0.savedFlowerPos).add(0.0, 0.6F, 0.0);
            if (flowerPos.distanceTo(this.this$0.position()) > 1.0) {
               this.hoverPos = flowerPos;
               this.setWantedPos();
            } else {
               if (this.hoverPos == null) {
                  this.hoverPos = flowerPos;
               }

               boolean arrivedAtHoverPos = this.this$0.position().distanceTo(this.hoverPos) <= 0.1;
               boolean shouldSetWantedPos = true;
               if (!arrivedAtHoverPos && this.pollinatingTicks > 600) {
                  this.this$0.dropFlower();
               } else {
                  if (arrivedAtHoverPos) {
                     boolean shouldChangeHoverPositions = Bee.access$2200(this.this$0).nextInt(25) == 0;
                     if (shouldChangeHoverPositions) {
                        this.hoverPos = new Vec3(flowerPos.x() + this.getOffset(), flowerPos.y(), flowerPos.z() + this.getOffset());
                        Bee.access$2300(this.this$0).stop();
                     } else {
                        shouldSetWantedPos = false;
                     }

                     this.this$0.getLookControl().setLookAt(flowerPos.x(), flowerPos.y(), flowerPos.z());
                  }

                  if (shouldSetWantedPos) {
                     this.setWantedPos();
                  }

                  this.successfulPollinatingTicks++;
                  if (Bee.access$2400(this.this$0).nextFloat() < 0.05F && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
                     this.lastSoundPlayedTick = this.successfulPollinatingTicks;
                     this.this$0.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
                  }
               }
            }
         }
      }
   }

   private void setWantedPos() {
      this.this$0.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), 0.35F);
   }

   private float getOffset() {
      return (Bee.access$2500(this.this$0).nextFloat() * 2.0F - 1.0F) * 0.33333334F;
   }

   private Optional<BlockPos> findNearbyFlower() {
      Long2LongOpenHashMap tempCache = new Long2LongOpenHashMap();
      Optional<BlockPos> result = this.this$0
         .level()
         .findBlocksInBoxByManhattanDistance(this.this$0.blockPosition(), 5)
         .filterState(Bee::attractsBees)
         .findFirst((pos, var3) -> {
            long unreachableUntilTime = this.unreachableFlowerCache.getOrDefault(pos.asLong(), Long.MIN_VALUE);
            if (this.this$0.level().getGameTime() < unreachableUntilTime) {
               tempCache.put(pos.asLong(), unreachableUntilTime);
               return false;
            } else {
               Path path = Bee.access$2600(this.this$0).createPath(pos, 1);
               if (path != null && path.canReach()) {
                  return true;
               } else {
                  tempCache.put(pos.asLong(), this.this$0.level().getGameTime() + 600L);
                  return false;
               }
            }
         });
      this.unreachableFlowerCache = tempCache;
      return result;
   }
}
