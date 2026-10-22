package net.minecraft.world.entity.animal.polarbear;

import java.util.Objects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

class PolarBear$PolarBearHurtByTargetGoal extends HurtByTargetGoal {
   public PolarBear$PolarBearHurtByTargetGoal(final PolarBear this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public void start() {
      super.start();
      if (this.this$0.isBaby()) {
         this.alertOthers();
         this.stop();
      }
   }

   @Override
   protected void alertOther(final Mob other, final LivingEntity hurtByMob) {
      if (other instanceof PolarBear && !other.isBaby()) {
         super.alertOther(other, hurtByMob);
      }
   }
}
