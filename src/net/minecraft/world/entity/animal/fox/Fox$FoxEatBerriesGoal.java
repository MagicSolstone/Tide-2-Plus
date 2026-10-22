package net.minecraft.world.entity.animal.fox;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.level.gamerules.GameRules;

public class Fox$FoxEatBerriesGoal extends MoveToBlockGoal {
   private static final int WAIT_TICKS = 40;
   protected int ticksWaited;

   public Fox$FoxEatBerriesGoal(final Fox this$0, final double speedModifier, final int searchRange, final int verticalSearchRange) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(this$0, speedModifier, searchRange, verticalSearchRange);
   }

   @Override
   public double acceptedDistance() {
      return 2.0;
   }

   @Override
   public boolean shouldRecalculatePath() {
      return this.tryTicks % 100 == 0;
   }

   @Override
   protected boolean isValidTarget(final LevelReader level, final BlockPos pos) {
      BlockState blockState = level.getBlockState(pos);
      return blockState.is(Blocks.SWEET_BERRY_BUSH) && blockState.getValue(SweetBerryBushBlock.AGE) >= 2 || CaveVines.hasGlowBerries(blockState);
   }

   @Override
   public void tick() {
      if (this.isReachedTarget()) {
         if (this.ticksWaited >= 40) {
            this.onReachedTarget();
         } else {
            this.ticksWaited++;
         }
      } else if (!this.isReachedTarget() && Fox.access$300(this.this$0).nextFloat() < 0.05F) {
         this.this$0.playSound(SoundEvents.FOX_SNIFF, 1.0F, 1.0F);
      }

      super.tick();
   }

   protected void onReachedTarget() {
      if (getServerLevel(this.this$0.level()).getGameRules().get(GameRules.MOB_GRIEFING)) {
         BlockState state = this.this$0.level().getBlockState(this.blockPos);
         if (state.is(Blocks.SWEET_BERRY_BUSH)) {
            this.pickSweetBerries(state);
         } else if (CaveVines.hasGlowBerries(state)) {
            this.pickGlowBerry(state);
         }
      }
   }

   private void pickGlowBerry(final BlockState state) {
      CaveVines.use(this.this$0, state, this.this$0.level(), this.blockPos);
   }

   private void pickSweetBerries(final BlockState state) {
      int age = state.getValue(SweetBerryBushBlock.AGE);
      state.setValue(SweetBerryBushBlock.AGE, 1);
      int count = 1 + this.this$0.level().getRandom().nextInt(2) + (age == 3 ? 1 : 0);
      ItemStack heldItem = this.this$0.getItemBySlot(EquipmentSlot.MAINHAND);
      if (heldItem.isEmpty()) {
         this.this$0.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.SWEET_BERRIES));
         count--;
      }

      if (count > 0) {
         Block.popResource(this.this$0.level(), this.blockPos, new ItemStack(Items.SWEET_BERRIES, count));
      }

      this.this$0.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
      this.this$0.level().setBlock(this.blockPos, state.setValue(SweetBerryBushBlock.AGE, 1), 2);
      this.this$0.level().gameEvent(GameEvent.BLOCK_CHANGE, this.blockPos, GameEvent$Context.of(this.this$0));
   }

   @Override
   public boolean canUse() {
      return !this.this$0.isSleeping() && super.canUse();
   }

   @Override
   public void start() {
      this.ticksWaited = 0;
      this.this$0.setSitting(false);
      super.start();
   }
}
