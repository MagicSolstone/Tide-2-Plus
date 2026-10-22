package net.minecraft.world.entity.animal.turtle;

import net.minecraft.world.entity.ai.goal.RandomStrollGoal;

class Turtle$TurtleRandomStrollGoal extends RandomStrollGoal {
   private final Turtle turtle;

   private Turtle$TurtleRandomStrollGoal(final Turtle turtle, final double speedModifier, final int interval) {
      super(turtle, speedModifier, interval);
      this.turtle = turtle;
   }

   @Override
   public boolean canUse() {
      return !this.mob.isInWater() && !this.turtle.goingHome && !this.turtle.hasEgg() ? super.canUse() : false;
   }
}
