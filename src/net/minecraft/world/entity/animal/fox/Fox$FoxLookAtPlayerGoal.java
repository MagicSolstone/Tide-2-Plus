package net.minecraft.world.entity.animal.fox;

import java.util.Objects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;

class Fox$FoxLookAtPlayerGoal extends LookAtPlayerGoal {
   public Fox$FoxLookAtPlayerGoal(final Mob this$0, final Class<? extends LivingEntity> mob, final float lookAtType, final float lookDistance) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(mob, lookAtType, lookDistance);
   }

   @Override
   public boolean canUse() {
      return super.canUse() && !this.this$0.isFaceplanted() && !this.this$0.isInterested();
   }

   @Override
   public boolean canContinueToUse() {
      return super.canContinueToUse() && !this.this$0.isFaceplanted() && !this.this$0.isInterested();
   }
}
