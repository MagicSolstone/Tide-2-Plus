package net.minecraft.world.entity.animal.feline;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

class Cat$CatRelaxOnOwnerGoal extends Goal {
   private final Cat cat;
   @Nullable
   private Player ownerPlayer;
   @Nullable
   private BlockPos goalPos;
   private int onBedTicks;

   public Cat$CatRelaxOnOwnerGoal(final Cat cat) {
      this.cat = cat;
   }

   @Override
   public boolean canUse() {
      if (!this.cat.isTame()) {
         return false;
      } else if (this.cat.isOrderedToSit()) {
         return false;
      } else {
         LivingEntity owner = this.cat.getOwner();
         if (owner instanceof Player playerOwner) {
            this.ownerPlayer = playerOwner;
            if (!owner.isSleeping()) {
               return false;
            }

            if (this.cat.distanceToSqr(this.ownerPlayer) > 100.0) {
               return false;
            }

            BlockPos ownerPos = this.ownerPlayer.blockPosition();
            BlockState ownerPosState = this.cat.level().getBlockState(ownerPos);
            if (ownerPosState.is(BlockTags.CATS_CAN_LIE_ON)) {
               this.goalPos = ownerPosState.getOptionalValue(BedBlock.FACING)
                  .map(bedDir -> ownerPos.relative(bedDir.getOpposite()))
                  .orElseGet(ownerPos::immutable);
               return !this.spaceIsOccupied();
            }
         }

         return false;
      }
   }

   private boolean spaceIsOccupied() {
      for (Cat otherCat : this.cat.level().getEntitiesOfClass(Cat.class, new AABB(this.goalPos).inflate(2.0))) {
         if (otherCat != this.cat && (otherCat.isLying() || otherCat.isRelaxStateOne())) {
            return true;
         }
      }

      return false;
   }

   @Override
   public boolean canContinueToUse() {
      return this.cat.isTame()
         && !this.cat.isOrderedToSit()
         && this.ownerPlayer != null
         && this.ownerPlayer.isSleeping()
         && this.goalPos != null
         && !this.spaceIsOccupied();
   }

   @Override
   public void start() {
      if (this.goalPos != null) {
         this.cat.setInSittingPose(false);
         this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.1F);
      }
   }

   @Override
   public void stop() {
      this.cat.setLying(false);
      if (this.ownerPlayer.getSleepTimer() >= 100
         && this.cat.level().getRandom().nextFloat()
            < this.cat.level().environmentAttributes().getValue(EnvironmentAttributes.CAT_WAKING_UP_GIFT_CHANCE, this.cat.position())) {
         this.giveMorningGift();
      }

      this.onBedTicks = 0;
      this.cat.setRelaxStateOne(false);
      this.cat.getNavigation().stop();
   }

   private void giveMorningGift() {
      RandomSource random = this.cat.getRandom();
      BlockPos$MutableBlockPos catPos = new BlockPos$MutableBlockPos();
      catPos.set(this.cat.isLeashed() ? this.cat.getLeashHolder().blockPosition() : this.cat.blockPosition());
      this.cat
         .randomTeleport(
            catPos.getX() + random.nextInt(11) - 5,
            catPos.getY() + random.nextInt(5) - 2,
            catPos.getZ() + random.nextInt(11) - 5,
            false,
            BlockTags.CAT_DOES_NOT_TELEPORT_TO
         );
      catPos.set(this.cat.blockPosition());
      this.cat
         .dropFromGiftLootTable(
            getServerLevel(this.cat),
            BuiltInLootTables.CAT_MORNING_GIFT,
            (level, itemStack) -> level.addFreshEntity(
               new ItemEntity(
                  level,
                  (double)catPos.getX() - Mth.sin(this.cat.yBodyRot * (float) (Math.PI / 180.0)),
                  catPos.getY(),
                  (double)catPos.getZ() + Mth.cos(this.cat.yBodyRot * (float) (Math.PI / 180.0)),
                  itemStack
               )
            )
         );
   }

   @Override
   public void tick() {
      if (this.ownerPlayer != null && this.goalPos != null) {
         this.cat.setInSittingPose(false);
         this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.1F);
         if (this.cat.distanceToSqr(this.ownerPlayer) < 2.5) {
            this.onBedTicks++;
            if (this.onBedTicks > this.adjustedTickDelay(16)) {
               this.cat.setLying(true);
               this.cat.setRelaxStateOne(false);
            } else {
               this.cat.lookAt(this.ownerPlayer, 45.0F, 45.0F);
               this.cat.setRelaxStateOne(true);
            }
         } else {
            this.cat.setLying(false);
         }
      }
   }
}
