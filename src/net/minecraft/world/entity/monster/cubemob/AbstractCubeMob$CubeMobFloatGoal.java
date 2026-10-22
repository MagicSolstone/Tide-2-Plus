package net.minecraft.world.entity.monster.cubemob;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

class AbstractCubeMob$CubeMobFloatGoal extends Goal {
   private final AbstractCubeMob cubeMob;

   public AbstractCubeMob$CubeMobFloatGoal(final AbstractCubeMob mob) {
      this.cubeMob = mob;
      this.setFlags(EnumSet.of(Goal$Flag.JUMP, Goal$Flag.MOVE));
      mob.getNavigation().setCanFloat(true);
   }

   @Override
   public boolean canUse() {
      return this.cubeMob.isInLiquid() && this.cubeMob.getMoveControl() instanceof AbstractCubeMob$CubeMobMoveControl;
   }

   @Override
   public boolean requiresUpdateEveryTick() {
      return true;
   }

   @Override
   public void tick() {
      if (this.cubeMob.getRandom().nextFloat() < 0.8F) {
         this.cubeMob.getJumpControl().jump();
      }

      if (this.cubeMob.getMoveControl() instanceof AbstractCubeMob$CubeMobMoveControl cubeMobMoveControl) {
         cubeMobMoveControl.setWantedMovement(1.2);
      }
   }
}
