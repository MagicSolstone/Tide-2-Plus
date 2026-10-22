package net.minecraft.world.entity.animal.bee;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;

class Bee$BeeGrowCropGoal extends Bee$BaseBeeGoal {
   private static final int GROW_CHANCE = 30;

   private Bee$BeeGrowCropGoal(final Bee this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public boolean canBeeUse() {
      if (this.this$0.getCropsGrownSincePollination() >= 10) {
         return false;
      } else {
         return Bee.access$2700(this.this$0).nextFloat() < 0.3F ? false : this.this$0.hasNectar() && this.this$0.isHiveValid();
      }
   }

   @Override
   public boolean canBeeContinueToUse() {
      return this.canBeeUse();
   }

   @Override
   public void tick() {
      if (Bee.access$2800(this.this$0).nextInt(this.adjustedTickDelay(30)) == 0) {
         for (int i = 1; i <= 2; i++) {
            BlockPos belowPos = this.this$0.blockPosition().below(i);
            BlockState belowState = this.this$0.level().getBlockState(belowPos);
            Block belowBlock = belowState.getBlock();
            BlockState growState = null;
            if (belowState.is(BlockTags.BEE_GROWABLES)) {
               if (belowBlock instanceof CropBlock cropBlockBelow) {
                  if (!cropBlockBelow.isMaxAge(belowState)) {
                     growState = cropBlockBelow.getStateForAge(cropBlockBelow.getAge(belowState) + 1);
                  }
               } else if (belowBlock instanceof StemBlock) {
                  int age = belowState.getValue(StemBlock.AGE);
                  if (age < 7) {
                     growState = belowState.setValue(StemBlock.AGE, age + 1);
                  }
               } else if (belowState.is(Blocks.SWEET_BERRY_BUSH)) {
                  int age = belowState.getValue(SweetBerryBushBlock.AGE);
                  if (age < 3) {
                     growState = belowState.setValue(SweetBerryBushBlock.AGE, age + 1);
                  }
               } else if (belowState.is(Blocks.CAVE_VINES) || belowState.is(Blocks.CAVE_VINES_PLANT)) {
                  BonemealableBlock bonemealableBlock = (BonemealableBlock)belowState.getBlock();
                  if (bonemealableBlock.isValidBonemealTarget(this.this$0.level(), belowPos, belowState, BonemealSource.MOB)) {
                     bonemealableBlock.performBonemeal((ServerLevel)this.this$0.level(), Bee.access$2900(this.this$0), belowPos, belowState, BonemealSource.MOB);
                     growState = this.this$0.level().getBlockState(belowPos);
                  }
               }

               if (growState != null) {
                  this.this$0.level().levelEvent(2011, belowPos, 15);
                  this.this$0.level().setBlockAndUpdate(belowPos, growState);
                  this.this$0.incrementNumCropsGrownSincePollination();
               }
            }
         }
      }
   }
}
