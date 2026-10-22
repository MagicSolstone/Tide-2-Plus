package net.minecraft.world.entity.monster;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction$Axis;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.phys.AABB;

class Shulker$ShulkerDefenseAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
   public Shulker$ShulkerDefenseAttackGoal(final Shulker mob) {
      super(mob, LivingEntity.class, 10, true, false, (input, level) -> input instanceof Enemy);
   }

   @Override
   public boolean canUse() {
      return this.mob.getTeam() == null ? false : super.canUse();
   }

   @Override
   protected AABB getTargetSearchArea(final double followDistance) {
      Direction attachFace = ((Shulker)this.mob).getAttachFace();
      if (attachFace.getAxis() == Direction$Axis.X) {
         return this.mob.getBoundingBox().inflate(4.0, followDistance, followDistance);
      } else {
         return attachFace.getAxis() == Direction$Axis.Z
            ? this.mob.getBoundingBox().inflate(followDistance, followDistance, 4.0)
            : this.mob.getBoundingBox().inflate(followDistance, 4.0, followDistance);
      }
   }
}
