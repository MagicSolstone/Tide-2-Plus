package net.minecraft.world.entity.monster;

import java.util.Objects;
import net.minecraft.world.entity.ai.goal.Goal;

class Shulker$ShulkerPeekGoal extends Goal {
   private int peekTime;

   private Shulker$ShulkerPeekGoal(final Shulker this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
   }

   @Override
   public boolean canUse() {
      return this.this$0.getTarget() == null
         && Shulker.access$000(this.this$0).nextInt(reducedTickDelay(40)) == 0
         && this.this$0.canStayAt(this.this$0.blockPosition(), this.this$0.getAttachFace());
   }

   @Override
   public boolean canContinueToUse() {
      return this.this$0.getTarget() == null && this.peekTime > 0;
   }

   @Override
   public void start() {
      this.peekTime = this.adjustedTickDelay(20 * (1 + Shulker.access$100(this.this$0).nextInt(3)));
      this.this$0.setRawPeekAmount(30);
   }

   @Override
   public void stop() {
      if (this.this$0.getTarget() == null) {
         this.this$0.setRawPeekAmount(0);
      }
   }

   @Override
   public void tick() {
      this.peekTime--;
   }
}
