package net.minecraft.world.entity.monster.skeleton;

import java.util.Objects;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

class AbstractSkeleton$1 extends MeleeAttackGoal {
   AbstractSkeleton$1(final AbstractSkeleton this$0, final PathfinderMob mob, final double speedModifier, final boolean followingTargetEvenIfNotSeen) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(mob, speedModifier, followingTargetEvenIfNotSeen);
   }

   @Override
   public void stop() {
      super.stop();
      this.this$0.setAggressive(false);
   }

   @Override
   public void start() {
      super.start();
      this.this$0.setAggressive(true);
   }
}
