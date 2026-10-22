package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.phys.Vec3;

class Blaze$BlazeAttackGoal extends Goal {
   private final Blaze blaze;
   private int attackStep;
   private int attackTime;
   private int lastSeen;

   public Blaze$BlazeAttackGoal(final Blaze blaze) {
      this.blaze = blaze;
      this.setFlags(EnumSet.of(Goal$Flag.MOVE, Goal$Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      LivingEntity target = this.blaze.getTarget();
      return target != null && target.isAlive() && this.blaze.canAttack(target);
   }

   @Override
   public void start() {
      this.attackStep = 0;
   }

   @Override
   public void stop() {
      this.blaze.setCharged(false);
      this.lastSeen = 0;
   }

   @Override
   public boolean requiresUpdateEveryTick() {
      return true;
   }

   @Override
   public void tick() {
      this.attackTime--;
      LivingEntity target = this.blaze.getTarget();
      if (target != null) {
         boolean hasLineOfSight = this.blaze.getSensing().hasLineOfSight(target);
         if (hasLineOfSight) {
            this.lastSeen = 0;
         } else {
            this.lastSeen++;
         }

         double distance = this.blaze.distanceToSqr(target);
         if (distance < 4.0) {
            if (!hasLineOfSight) {
               return;
            }

            if (this.attackTime <= 0) {
               this.attackTime = 20;
               this.blaze.doHurtTarget(getServerLevel(this.blaze), target);
            }

            this.blaze.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.0);
         } else if (distance < this.getFollowDistance() * this.getFollowDistance() && hasLineOfSight) {
            double xd = target.getX() - this.blaze.getX();
            double yd = target.getY(0.5) - this.blaze.getY(0.5);
            double zd = target.getZ() - this.blaze.getZ();
            if (this.attackTime <= 0) {
               this.attackStep++;
               if (this.attackStep == 1) {
                  this.attackTime = 60;
                  this.blaze.setCharged(true);
               } else if (this.attackStep <= 4) {
                  this.attackTime = 6;
               } else {
                  this.attackTime = 100;
                  this.attackStep = 0;
                  this.blaze.setCharged(false);
               }

               if (this.attackStep > 1) {
                  double sqd = Math.sqrt(Math.sqrt(distance)) * 0.5;
                  if (!this.blaze.isSilent()) {
                     this.blaze.level().levelEvent(null, 1018, this.blaze.blockPosition(), 0);
                  }

                  for (int i = 0; i < 1; i++) {
                     Vec3 direction = new Vec3(this.blaze.getRandom().triangle(xd, 2.297 * sqd), yd, this.blaze.getRandom().triangle(zd, 2.297 * sqd));
                     SmallFireball entity = new SmallFireball(this.blaze.level(), this.blaze, direction.normalize());
                     entity.setPos(entity.getX(), this.blaze.getY(0.5) + 0.5, entity.getZ());
                     this.blaze.level().addFreshEntity(entity);
                  }
               }
            }

            this.blaze.getLookControl().setLookAt(target, 10.0F, 10.0F);
         } else if (this.lastSeen < 5) {
            this.blaze.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.0);
         }

         super.tick();
      }
   }

   private double getFollowDistance() {
      return this.blaze.getAttributeValue(Attributes.FOLLOW_RANGE);
   }
}
