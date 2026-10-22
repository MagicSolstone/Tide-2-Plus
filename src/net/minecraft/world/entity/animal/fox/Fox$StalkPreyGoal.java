package net.minecraft.world.entity.animal.fox;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

class Fox$StalkPreyGoal extends Goal {
   public Fox$StalkPreyGoal(final Fox this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.setFlags(EnumSet.of(Goal$Flag.MOVE, Goal$Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      if (this.this$0.isSleeping()) {
         return false;
      } else {
         LivingEntity target = this.this$0.getTarget();
         return target != null
            && target.isAlive()
            && Fox.STALKABLE_PREY.test(target)
            && this.this$0.distanceToSqr(target) > 36.0
            && !this.this$0.isCrouching()
            && !this.this$0.isInterested()
            && !Fox.access$000(this.this$0);
      }
   }

   @Override
   public void start() {
      this.this$0.setSitting(false);
      this.this$0.setFaceplanted(false);
   }

   @Override
   public void stop() {
      LivingEntity target = this.this$0.getTarget();
      if (target != null && Fox.isPathClear(this.this$0, target)) {
         this.this$0.setIsInterested(true);
         this.this$0.setIsCrouching(true);
         this.this$0.getNavigation().stop();
         this.this$0.getLookControl().setLookAt(target, this.this$0.getMaxHeadYRot(), this.this$0.getMaxHeadXRot());
      } else {
         this.this$0.setIsInterested(false);
         this.this$0.setIsCrouching(false);
      }
   }

   @Override
   public void tick() {
      LivingEntity target = this.this$0.getTarget();
      if (target != null) {
         this.this$0.getLookControl().setLookAt(target, this.this$0.getMaxHeadYRot(), this.this$0.getMaxHeadXRot());
         if (this.this$0.distanceToSqr(target) <= 36.0) {
            this.this$0.setIsInterested(true);
            this.this$0.setIsCrouching(true);
            this.this$0.getNavigation().stop();
         } else {
            this.this$0.getNavigation().moveTo(target, 1.5);
         }
      }
   }
}
