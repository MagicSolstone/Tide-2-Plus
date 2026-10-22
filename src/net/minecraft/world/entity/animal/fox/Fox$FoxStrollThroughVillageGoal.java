package net.minecraft.world.entity.animal.fox;

import java.util.Objects;
import net.minecraft.world.entity.ai.goal.StrollThroughVillageGoal;

class Fox$FoxStrollThroughVillageGoal extends StrollThroughVillageGoal {
   public Fox$FoxStrollThroughVillageGoal(final Fox this$0, final int searchRadius, final int interval) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x, interval);
   }

   @Override
   public void start() {
      this.this$0.clearStates();
      super.start();
   }

   @Override
   public boolean canUse() {
      return super.canUse() && this.canFoxMove();
   }

   @Override
   public boolean canContinueToUse() {
      return super.canContinueToUse() && this.canFoxMove();
   }

   private boolean canFoxMove() {
      return !this.this$0.isSleeping() && !this.this$0.isSitting() && !this.this$0.isDefending() && this.this$0.getTarget() == null;
   }
}
