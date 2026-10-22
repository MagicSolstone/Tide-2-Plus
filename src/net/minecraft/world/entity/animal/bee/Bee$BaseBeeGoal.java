package net.minecraft.world.entity.animal.bee;

import java.util.Objects;
import net.minecraft.world.entity.ai.goal.Goal;

abstract class Bee$BaseBeeGoal extends Goal {
   private Bee$BaseBeeGoal(final Bee this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
   }

   public abstract boolean canBeeUse();

   public abstract boolean canBeeContinueToUse();

   @Override
   public boolean canUse() {
      return this.canBeeUse() && !this.this$0.isAngry();
   }

   @Override
   public boolean canContinueToUse() {
      return this.canBeeContinueToUse() && !this.this$0.isAngry();
   }
}
