package net.minecraft.world.entity.animal.turtle;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;

class Turtle$TurtleLayEggGoal extends MoveToBlockGoal {
   private final Turtle turtle;

   public Turtle$TurtleLayEggGoal(final Turtle turtle, final double speedModifier) {
      super(turtle, speedModifier, 16);
      this.turtle = turtle;
   }

   @Override
   public boolean canUse() {
      return this.turtle.hasEgg() && this.turtle.homePos.closerToCenterThan(this.turtle.position(), 9.0) ? super.canUse() : false;
   }

   @Override
   public boolean canContinueToUse() {
      return super.canContinueToUse() && this.turtle.hasEgg() && this.turtle.homePos.closerToCenterThan(this.turtle.position(), 9.0);
   }

   @Override
   public void tick() {
      super.tick();
      BlockPos turtlePos = this.turtle.blockPosition();
      if (!this.turtle.isInWater() && this.isReachedTarget()) {
         if (this.turtle.layEggCounter < 1) {
            this.turtle.setLayingEgg(true);
         } else if (this.turtle.layEggCounter > this.adjustedTickDelay(200)) {
            Level level = this.turtle.level();
            level.playSound(null, turtlePos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + level.getRandom().nextFloat() * 0.2F);
            BlockPos eggPos = this.blockPos.above();
            BlockState eggState = Blocks.TURTLE_EGG.defaultBlockState().setValue(TurtleEggBlock.EGGS, Turtle.access$100(this.turtle).nextInt(4) + 1);
            level.setBlockAndUpdate(eggPos, eggState);
            level.gameEvent(GameEvent.BLOCK_PLACE, eggPos, GameEvent$Context.of(this.turtle, eggState));
            this.turtle.setHasEgg(false);
            this.turtle.setLayingEgg(false);
            this.turtle.setInLoveTime(600);
         }

         if (this.turtle.isLayingEgg()) {
            this.turtle.layEggCounter++;
         }
      }
   }

   @Override
   protected boolean isValidTarget(final LevelReader level, final BlockPos pos) {
      return !level.isEmptyBlock(pos.above()) ? false : TurtleEggBlock.isSand(level, pos);
   }
}
