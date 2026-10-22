package net.minecraft.world.entity.animal.panda;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;

class Panda$PandaLookAtPlayerGoal extends LookAtPlayerGoal {
   private final Panda panda;

   public Panda$PandaLookAtPlayerGoal(final Panda mob, final Class<? extends LivingEntity> lookAtType, final float lookDistance) {
      super(mob, lookAtType, lookDistance);
      this.panda = mob;
   }

   public void setTarget(final LivingEntity entity) {
      this.lookAt = entity;
   }

   @Override
   public boolean canContinueToUse() {
      return this.lookAt != null && super.canContinueToUse();
   }

   @Override
   public boolean canUse() {
      if (this.mob.getRandom().nextFloat() >= this.probability) {
         return false;
      } else {
         if (this.lookAt == null) {
            ServerLevel level = getServerLevel(this.mob);
            if (this.lookAtType == Player.class) {
               this.lookAt = level.getNearestPlayer(this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
            } else {
               this.lookAt = level.getNearestEntity(
                  this.mob
                     .level()
                     .getEntitiesOfClass(this.lookAtType, this.mob.getBoundingBox().inflate(this.lookDistance, 3.0, this.lookDistance), entity -> true),
                  this.lookAtContext,
                  this.mob,
                  this.mob.getX(),
                  this.mob.getEyeY(),
                  this.mob.getZ()
               );
            }
         }

         return this.panda.canPerformAction() && this.lookAt != null;
      }
   }

   @Override
   public void tick() {
      if (this.lookAt != null) {
         super.tick();
      }
   }
}
