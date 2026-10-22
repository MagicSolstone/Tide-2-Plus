package net.minecraft.world.entity.animal.fox;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

class Fox$PerchAndSearchGoal extends Fox$FoxBehaviorGoal {
   private double relX;
   private double relZ;
   private int lookTime;
   private int looksRemaining;

   public Fox$PerchAndSearchGoal(final Fox this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
      this.setFlags(EnumSet.of(Goal$Flag.MOVE, Goal$Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      return this.this$0.getLastHurtByMob() == null
         && this.this$0.getRandom().nextFloat() < 0.02F
         && !this.this$0.isSleeping()
         && this.this$0.getTarget() == null
         && this.this$0.getNavigation().isDone()
         && !this.alertable()
         && !this.this$0.isPouncing()
         && !this.this$0.isCrouching();
   }

   @Override
   public boolean canContinueToUse() {
      return this.looksRemaining > 0;
   }

   @Override
   public void start() {
      this.resetLook();
      this.looksRemaining = 2 + this.this$0.getRandom().nextInt(3);
      this.this$0.setSitting(true);
      this.this$0.getNavigation().stop();
   }

   @Override
   public void stop() {
      this.this$0.setSitting(false);
   }

   @Override
   public void tick() {
      this.lookTime--;
      if (this.lookTime <= 0) {
         this.looksRemaining--;
         this.resetLook();
      }

      this.this$0
         .getLookControl()
         .setLookAt(
            this.this$0.getX() + this.relX, this.this$0.getEyeY(), this.this$0.getZ() + this.relZ, this.this$0.getMaxHeadYRot(), this.this$0.getMaxHeadXRot()
         );
   }

   private void resetLook() {
      double rnd = (Math.PI * 2) * this.this$0.getRandom().nextDouble();
      this.relX = Math.cos(rnd);
      this.relZ = Math.sin(rnd);
      this.lookTime = this.adjustedTickDelay(80 + this.this$0.getRandom().nextInt(20));
   }
}
