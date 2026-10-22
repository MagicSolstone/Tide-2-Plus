package net.minecraft.world.entity.raid;

import java.util.EnumSet;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.illager.AbstractIllager;

public class Raider$HoldGroundAttackGoal extends Goal {
   private final Raider mob;
   private final float hostileRadiusSqr;
   public final TargetingConditions shoutTargeting = TargetingConditions.forNonCombat().range(8.0).ignoreLineOfSight().ignoreInvisibilityTesting();

   public Raider$HoldGroundAttackGoal(final AbstractIllager mob, final float hostileRadius) {
      this.mob = mob;
      this.hostileRadiusSqr = hostileRadius * hostileRadius;
      this.setFlags(EnumSet.of(Goal$Flag.MOVE, Goal$Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      LivingEntity lastHurtByMob = this.mob.getLastHurtByMob();
      return this.mob.getCurrentRaid() == null
         && Raider.access$200(this.mob)
         && this.mob.getTarget() != null
         && !this.mob.isAggressive()
         && (lastHurtByMob == null || !lastHurtByMob.is(EntityTypes.PLAYER));
   }

   @Override
   public void start() {
      super.start();
      this.mob.getNavigation().stop();

      for (Raider entity : getServerLevel(this.mob)
         .getNearbyEntities(Raider.class, this.shoutTargeting, this.mob, this.mob.getBoundingBox().inflate(8.0, 8.0, 8.0))) {
         entity.setTarget(this.mob.getTarget());
      }
   }

   @Override
   public void stop() {
      super.stop();
      LivingEntity target = this.mob.getTarget();
      if (target != null) {
         for (Raider entity : getServerLevel(this.mob)
            .getNearbyEntities(Raider.class, this.shoutTargeting, this.mob, this.mob.getBoundingBox().inflate(8.0, 8.0, 8.0))) {
            entity.setTarget(target);
            entity.setAggressive(true);
         }

         this.mob.setAggressive(true);
      }
   }

   @Override
   public boolean requiresUpdateEveryTick() {
      return true;
   }

   @Override
   public void tick() {
      LivingEntity target = this.mob.getTarget();
      if (target != null) {
         if (this.mob.distanceToSqr(target) > this.hostileRadiusSqr) {
            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (Raider.access$300(this.mob).nextInt(50) == 0) {
               this.mob.playAmbientSound();
            }
         } else {
            this.mob.setAggressive(true);
         }

         super.tick();
      }
   }
}
