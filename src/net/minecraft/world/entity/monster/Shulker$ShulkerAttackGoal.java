package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.projectile.ShulkerBullet;

class Shulker$ShulkerAttackGoal extends Goal {
   private int attackTime;

   public Shulker$ShulkerAttackGoal(final Shulker this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.setFlags(EnumSet.of(Goal$Flag.MOVE, Goal$Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      LivingEntity target = this.this$0.getTarget();
      return target != null && target.isAlive() ? this.this$0.level().getDifficulty() != Difficulty.PEACEFUL : false;
   }

   @Override
   public void start() {
      this.attackTime = 20;
      this.this$0.setRawPeekAmount(100);
   }

   @Override
   public void stop() {
      this.this$0.setRawPeekAmount(0);
   }

   @Override
   public boolean requiresUpdateEveryTick() {
      return true;
   }

   @Override
   public void tick() {
      if (this.this$0.level().getDifficulty() != Difficulty.PEACEFUL) {
         this.attackTime--;
         LivingEntity target = this.this$0.getTarget();
         if (target != null) {
            this.this$0.getLookControl().setLookAt(target, 180.0F, 180.0F);
            double distance = this.this$0.distanceToSqr(target);
            if (distance < 400.0) {
               if (this.attackTime <= 0) {
                  this.attackTime = 20 + Shulker.access$200(this.this$0).nextInt(10) * 20 / 2;
                  this.this$0.level().addFreshEntity(new ShulkerBullet(this.this$0.level(), this.this$0, target, this.this$0.getAttachFace().getAxis()));
                  this.this$0
                     .playSound(
                        SoundEvents.SHULKER_SHOOT,
                        2.0F,
                        (Shulker.access$300(this.this$0).nextFloat() - Shulker.access$400(this.this$0).nextFloat()) * 0.2F + 1.0F
                     );
               }
            } else {
               this.this$0.setTarget(null);
            }

            super.tick();
         }
      }
   }
}
