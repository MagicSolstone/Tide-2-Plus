package net.minecraft.world.entity.animal.bee;

import java.util.Objects;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

class Bee$BeeAttackGoal extends MeleeAttackGoal {
   public Bee$BeeAttackGoal(final Bee this$0, final PathfinderMob mob, final double speedModifier, final boolean trackTarget) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(mob, speedModifier, trackTarget);
   }

   @Override
   public boolean canUse() {
      return super.canUse() && this.this$0.isAngry() && !this.this$0.hasStung();
   }

   @Override
   public boolean canContinueToUse() {
      return super.canContinueToUse() && this.this$0.isAngry() && !this.this$0.hasStung();
   }
}
