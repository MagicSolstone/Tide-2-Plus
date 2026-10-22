package net.minecraft.world.entity.animal.bee;

import java.util.Objects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

class Bee$BeeHurtByOtherGoal extends HurtByTargetGoal {
   public Bee$BeeHurtByOtherGoal(final Bee this$0, final Bee bee) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(bee);
   }

   @Override
   public boolean canContinueToUse() {
      return this.this$0.isAngry() && super.canContinueToUse();
   }

   @Override
   protected void alertOther(final Mob other, final LivingEntity hurtByMob) {
      if (other instanceof Bee && this.mob.hasLineOfSight(hurtByMob)) {
         other.setTarget(hurtByMob);
      }
   }
}
