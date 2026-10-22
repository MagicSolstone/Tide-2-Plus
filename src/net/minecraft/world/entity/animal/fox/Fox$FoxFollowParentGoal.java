package net.minecraft.world.entity.animal.fox;

import net.minecraft.world.entity.ai.goal.FollowParentGoal;

class Fox$FoxFollowParentGoal extends FollowParentGoal {
   private final Fox fox;

   public Fox$FoxFollowParentGoal(final Fox fox, final double speedModifier) {
      super(fox, speedModifier);
      this.fox = fox;
   }

   @Override
   public boolean canUse() {
      return !this.fox.isDefending() && super.canUse();
   }

   @Override
   public boolean canContinueToUse() {
      return !this.fox.isDefending() && super.canContinueToUse();
   }

   @Override
   public void start() {
      this.fox.clearStates();
      super.start();
   }
}
