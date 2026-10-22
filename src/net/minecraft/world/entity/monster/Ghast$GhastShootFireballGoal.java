package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

class Ghast$GhastShootFireballGoal extends Goal {
   private final Ghast ghast;
   public int chargeTime;

   public Ghast$GhastShootFireballGoal(final Ghast ghast) {
      this.ghast = ghast;
   }

   @Override
   public boolean canUse() {
      return this.ghast.getTarget() != null;
   }

   @Override
   public void start() {
      this.chargeTime = 0;
   }

   @Override
   public void stop() {
      this.ghast.setCharging(false);
   }

   @Override
   public boolean requiresUpdateEveryTick() {
      return true;
   }

   @Override
   public void tick() {
      LivingEntity target = this.ghast.getTarget();
      if (target != null) {
         double maxDist = 64.0;
         if (target.distanceToSqr(this.ghast) < 4096.0 && this.ghast.hasLineOfSight(target)) {
            Level level = this.ghast.level();
            this.chargeTime++;
            if (this.chargeTime == 10 && !this.ghast.isSilent()) {
               level.levelEvent(null, 1015, this.ghast.blockPosition(), 0);
            }

            if (this.chargeTime == 20) {
               double d = 4.0;
               Vec3 viewVector = this.ghast.getViewVector(1.0F);
               double xdd = target.getX() - (this.ghast.getX() + viewVector.x * 4.0);
               double ydd = target.getY(0.5) - (0.5 + this.ghast.getY(0.5));
               double zdd = target.getZ() - (this.ghast.getZ() + viewVector.z * 4.0);
               Vec3 direction = new Vec3(xdd, ydd, zdd);
               if (!this.ghast.isSilent()) {
                  level.levelEvent(null, 1016, this.ghast.blockPosition(), 0);
               }

               LargeFireball entity = new LargeFireball(level, this.ghast, direction.normalize(), this.ghast.getExplosionPower());
               entity.setPos(this.ghast.getX() + viewVector.x * 4.0, this.ghast.getY(0.5) + 0.5, entity.getZ() + viewVector.z * 4.0);
               level.addFreshEntity(entity);
               this.chargeTime = -40;
            }
         } else if (this.chargeTime > 0) {
            this.chargeTime--;
         }

         this.ghast.setCharging(this.chargeTime > 10);
      }
   }
}
