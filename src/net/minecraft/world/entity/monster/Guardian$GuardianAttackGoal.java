package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

class Guardian$GuardianAttackGoal extends Goal {
   private final Guardian guardian;
   private int attackTime;
   private final boolean elder;

   public Guardian$GuardianAttackGoal(final Guardian guardian) {
      this.guardian = guardian;
      this.elder = guardian instanceof ElderGuardian;
      this.setFlags(EnumSet.of(Goal$Flag.MOVE, Goal$Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      LivingEntity target = this.guardian.getTarget();
      return target != null && target.isAlive();
   }

   @Override
   public boolean canContinueToUse() {
      return super.canContinueToUse() && (this.elder || this.guardian.getTarget() != null && this.guardian.distanceToSqr(this.guardian.getTarget()) > 9.0);
   }

   @Override
   public void start() {
      this.attackTime = -10;
      this.guardian.getNavigation().stop();
      LivingEntity target = this.guardian.getTarget();
      if (target != null) {
         this.guardian.getLookControl().setLookAt(target, 90.0F, 90.0F);
      }

      this.guardian.needsSync = true;
   }

   @Override
   public void stop() {
      this.guardian.setActiveAttackTarget(0);
      this.guardian.setTarget(null);
      this.guardian.randomStrollGoal.trigger();
   }

   @Override
   public boolean requiresUpdateEveryTick() {
      return true;
   }

   @Override
   public void tick() {
      LivingEntity target = this.guardian.getTarget();
      if (target != null) {
         this.guardian.getNavigation().stop();
         this.guardian.getLookControl().setLookAt(target, 90.0F, 90.0F);
         if (!this.guardian.hasLineOfSight(target)) {
            this.guardian.setTarget(null);
         } else {
            this.attackTime++;
            if (this.attackTime == 0) {
               this.guardian.setActiveAttackTarget(target.getId());
               if (!this.guardian.isSilent()) {
                  this.guardian.level().broadcastEntityEvent(this.guardian, (byte)21);
               }
            } else if (this.attackTime >= this.guardian.getAttackDuration()) {
               float magicDamage = 1.0F;
               if (this.guardian.level().getDifficulty() == Difficulty.HARD) {
                  magicDamage += 2.0F;
               }

               if (this.elder) {
                  magicDamage += 2.0F;
               }

               ServerLevel serverLevel = getServerLevel(this.guardian);
               target.hurtServer(serverLevel, this.guardian.damageSources().indirectMagic(this.guardian, this.guardian), magicDamage);
               this.guardian.doHurtTarget(serverLevel, target);
               this.guardian.setTarget(null);
            }

            super.tick();
         }
      }
   }
}
