package net.minecraft.world.entity.animal.happyghast;

import java.util.Objects;
import net.minecraft.world.entity.ai.goal.FloatGoal;

class HappyGhast$HappyGhastFloatGoal extends FloatGoal {
   public HappyGhast$HappyGhastFloatGoal(final HappyGhast this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public boolean canUse() {
      return !this.this$0.isOnStillTimeout() && super.canUse();
   }
}
