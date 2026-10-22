package net.minecraft.world.entity.animal.fox;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

class Fox$FaceplantGoal extends Goal {
   private int countdown;

   public Fox$FaceplantGoal(final Fox this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.setFlags(EnumSet.of(Goal$Flag.LOOK, Goal$Flag.JUMP, Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      return this.this$0.isFaceplanted();
   }

   @Override
   public boolean canContinueToUse() {
      return this.canUse() && this.countdown > 0;
   }

   @Override
   public void start() {
      this.countdown = this.adjustedTickDelay(40);
   }

   @Override
   public void stop() {
      this.this$0.setFaceplanted(false);
   }

   @Override
   public void tick() {
      this.countdown--;
   }
}
