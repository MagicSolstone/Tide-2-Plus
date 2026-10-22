package net.minecraft.world.entity.ai.goal;

class GoalSelector$2 extends WrappedGoal {
   GoalSelector$2(final int priority, final Goal goal) {
      super(priority, goal);
   }

   @Override
   public boolean isRunning() {
      return false;
   }
}
