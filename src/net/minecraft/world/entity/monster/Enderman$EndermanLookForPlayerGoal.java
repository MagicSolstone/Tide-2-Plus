package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.targeting.TargetingConditions$Selector;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

class Enderman$EndermanLookForPlayerGoal extends NearestAttackableTargetGoal<Player> {
   private final Enderman enderman;
   @Nullable
   private Player pendingTarget;
   private int aggroTime;
   private int teleportTime;
   private final TargetingConditions startAggroTargetConditions;
   private final TargetingConditions continueAggroTargetConditions = TargetingConditions.forCombat().ignoreLineOfSight();
   private final TargetingConditions$Selector isAngerInducing;

   public Enderman$EndermanLookForPlayerGoal(final Enderman enderman, @Nullable final TargetingConditions$Selector isAngryAt) {
      super(enderman, Player.class, 10, false, false, isAngryAt);
      this.enderman = enderman;
      this.isAngerInducing = (target, level) -> (enderman.isBeingStaredBy((Player)target) || enderman.isAngryAt(target, level))
         && !enderman.hasIndirectPassenger(target);
      this.startAggroTargetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(this.isAngerInducing);
   }

   @Override
   public boolean canUse() {
      this.pendingTarget = getServerLevel(this.enderman).getNearestPlayer(this.startAggroTargetConditions.range(this.getFollowDistance()), this.enderman);
      return this.pendingTarget != null;
   }

   @Override
   public void start() {
      this.aggroTime = this.adjustedTickDelay(5);
      this.teleportTime = 0;
      this.enderman.setBeingStaredAt();
   }

   @Override
   public void stop() {
      this.pendingTarget = null;
      super.stop();
   }

   @Override
   public boolean canContinueToUse() {
      if (this.pendingTarget != null) {
         if (!this.isAngerInducing.test(this.pendingTarget, getServerLevel(this.enderman))) {
            return false;
         } else {
            this.enderman.lookAt(this.pendingTarget, 10.0F, 10.0F);
            return true;
         }
      } else {
         if (this.target != null) {
            if (this.enderman.hasIndirectPassenger(this.target)) {
               return false;
            }

            if (this.continueAggroTargetConditions.test(getServerLevel(this.enderman), this.enderman, this.target)) {
               return true;
            }
         }

         return super.canContinueToUse();
      }
   }

   @Override
   public void tick() {
      if (this.enderman.getTarget() == null) {
         super.setTarget(null);
      }

      if (this.pendingTarget != null) {
         if (--this.aggroTime <= 0) {
            this.target = this.pendingTarget;
            this.pendingTarget = null;
            super.start();
         }
      } else {
         if (this.target != null && !this.enderman.isPassenger()) {
            if (this.enderman.isBeingStaredBy((Player)this.target)) {
               if (this.target.distanceToSqr(this.enderman) < 16.0) {
                  this.enderman.teleport();
               }

               this.teleportTime = 0;
            } else if (this.target.distanceToSqr(this.enderman) > 256.0
               && this.teleportTime++ >= this.adjustedTickDelay(30)
               && this.enderman.teleportTowards(this.target)) {
               this.teleportTime = 0;
            }
         }

         super.tick();
      }
   }
}
