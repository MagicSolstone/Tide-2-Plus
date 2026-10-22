package net.minecraft.world.entity.animal.fox;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

class Fox$SleepGoal extends Fox$FoxBehaviorGoal {
   private static final int WAIT_TIME_BEFORE_SLEEP = reducedTickDelay(140);
   private int countdown;

   public Fox$SleepGoal(final Fox this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
      this.countdown = Fox.access$100(this$0x).nextInt(WAIT_TIME_BEFORE_SLEEP);
      this.setFlags(EnumSet.of(Goal$Flag.MOVE, Goal$Flag.LOOK, Goal$Flag.JUMP));
   }

   @Override
   public boolean canUse() {
      return this.this$0.xxa == 0.0F && this.this$0.yya == 0.0F && this.this$0.zza == 0.0F ? this.canSleep() || this.this$0.isSleeping() : false;
   }

   @Override
   public boolean canContinueToUse() {
      return this.canSleep();
   }

   private boolean canSleep() {
      if (this.countdown > 0) {
         this.countdown--;
         return false;
      } else {
         return this.this$0.level().isBrightOutside() && this.hasShelter() && !this.alertable() && !this.this$0.isInPowderSnow;
      }
   }

   @Override
   public void stop() {
      this.countdown = Fox.access$200(this.this$0).nextInt(WAIT_TIME_BEFORE_SLEEP);
      this.this$0.clearStates();
   }

   @Override
   public void start() {
      this.this$0.setSitting(false);
      this.this$0.setIsCrouching(false);
      this.this$0.setIsInterested(false);
      this.this$0.setJumping(false);
      this.this$0.setSleeping(true);
      this.this$0.getNavigation().stop();
      this.this$0.getMoveControl().setWantedPosition(this.this$0.getX(), this.this$0.getY(), this.this$0.getZ(), 0.0);
   }
}
