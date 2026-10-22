package net.minecraft.world.entity.animal.fox;

import java.util.Objects;
import net.minecraft.world.entity.ai.goal.FloatGoal;

class Fox$FoxFloatGoal extends FloatGoal {
   public Fox$FoxFloatGoal(final Fox this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public void start() {
      super.start();
      this.this$0.clearStates();
   }

   @Override
   public boolean canUse() {
      return this.this$0.isInFluidDeeperThan(0.25, this.fluid) || this.this$0.isInLava();
   }
}
