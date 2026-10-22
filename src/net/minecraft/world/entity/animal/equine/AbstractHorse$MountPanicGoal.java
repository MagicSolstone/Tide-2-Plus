package net.minecraft.world.entity.animal.equine;

import java.util.Objects;
import net.minecraft.world.entity.ai.goal.PanicGoal;

class AbstractHorse$MountPanicGoal extends PanicGoal {
   public AbstractHorse$MountPanicGoal(final AbstractHorse this$0, final double speedModifier) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x, speedModifier);
   }

   @Override
   public boolean shouldPanic() {
      return !this.this$0.isMobControlled() && super.shouldPanic();
   }
}
