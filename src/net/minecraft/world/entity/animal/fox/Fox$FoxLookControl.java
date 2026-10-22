package net.minecraft.world.entity.animal.fox;

import java.util.Objects;
import net.minecraft.world.entity.ai.control.LookControl;

public class Fox$FoxLookControl extends LookControl {
   public Fox$FoxLookControl(final Fox this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(this$0);
   }

   @Override
   public void tick() {
      if (!this.this$0.isSleeping()) {
         super.tick();
      }
   }

   @Override
   protected boolean resetXRotOnTick() {
      return !this.this$0.isPouncing() && !this.this$0.isCrouching() && !this.this$0.isInterested() && !this.this$0.isFaceplanted();
   }
}
