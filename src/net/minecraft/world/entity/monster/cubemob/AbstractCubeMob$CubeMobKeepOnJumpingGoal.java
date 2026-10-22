package net.minecraft.world.entity.monster.cubemob;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

class AbstractCubeMob$CubeMobKeepOnJumpingGoal extends Goal {
   private final AbstractCubeMob cubeMob;

   public AbstractCubeMob$CubeMobKeepOnJumpingGoal(final AbstractCubeMob mob) {
      this.cubeMob = mob;
      this.setFlags(EnumSet.of(Goal$Flag.JUMP, Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      return !this.cubeMob.isPassenger();
   }

   @Override
   public void tick() {
      if (this.cubeMob.getMoveControl() instanceof AbstractCubeMob$CubeMobMoveControl cubeMobMoveControl) {
         cubeMobMoveControl.setWantedMovement(1.0);
      }
   }
}
