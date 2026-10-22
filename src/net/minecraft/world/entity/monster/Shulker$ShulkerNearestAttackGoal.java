package net.minecraft.world.entity.monster;

import java.util.Objects;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction$Axis;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

class Shulker$ShulkerNearestAttackGoal extends NearestAttackableTargetGoal<Player> {
   public Shulker$ShulkerNearestAttackGoal(final Shulker this$0, final Shulker mob) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(mob, Player.class, true);
   }

   @Override
   public boolean canUse() {
      return this.this$0.level().getDifficulty() == Difficulty.PEACEFUL ? false : super.canUse();
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
