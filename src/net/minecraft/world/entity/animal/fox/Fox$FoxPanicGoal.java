package net.minecraft.world.entity.animal.fox;

import java.util.Objects;
import net.minecraft.world.entity.ai.goal.PanicGoal;

class Fox$FoxPanicGoal extends PanicGoal {
   public Fox$FoxPanicGoal(final Fox this$0, final double speedModifier) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x, speedModifier);
   }

   @Override
   public boolean shouldPanic() {
      return !this.this$0.isDefending() && super.shouldPanic();
   }
}
