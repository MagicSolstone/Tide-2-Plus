package net.minecraft.world.entity.monster;

import java.util.Objects;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Targeting;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

class Vex$VexCopyOwnerTargetGoal extends TargetGoal {
   private final TargetingConditions copyOwnerTargeting;

   public Vex$VexCopyOwnerTargetGoal(final Vex this$0, final PathfinderMob mob) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(mob, false);
      this.copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
   }

   @Override
   public boolean canUse() {
      return this.this$0.getOwner() instanceof Targeting owner && owner.getTarget() != null && this.canAttack(owner.getTarget(), this.copyOwnerTargeting);
   }

   @Override
   public void start() {
      this.this$0.setTarget(this.this$0.getOwner() instanceof Targeting owner ? owner.getTarget() : null);
      super.start();
   }
}
