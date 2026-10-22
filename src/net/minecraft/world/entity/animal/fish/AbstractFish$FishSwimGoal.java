package net.minecraft.world.entity.animal.fish;

import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;

class AbstractFish$FishSwimGoal extends RandomSwimmingGoal {
   private final AbstractFish fish;

   public AbstractFish$FishSwimGoal(final AbstractFish fish) {
      super(fish, 1.0, 40);
      this.fish = fish;
   }

   @Override
   public boolean canUse() {
      return this.fish.canRandomSwim() && super.canUse();
   }
}
