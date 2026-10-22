package net.minecraft.world.entity.animal.turtle;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

class Turtle$TurtlePathNavigation extends AmphibiousPathNavigation {
   public Turtle$TurtlePathNavigation(final Turtle mob, final Level level) {
      super(mob, level);
   }

   @Override
   public boolean isStableDestination(final BlockPos pos) {
      return this.mob instanceof Turtle turtle && turtle.travelPos != null
         ? this.level.getBlockState(pos).is(Blocks.WATER)
         : !this.level.getBlockState(pos.below()).isAir();
   }
}
