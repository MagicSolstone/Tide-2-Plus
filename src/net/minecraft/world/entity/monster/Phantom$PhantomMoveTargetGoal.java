package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

abstract class Phantom$PhantomMoveTargetGoal extends Goal {
   public Phantom$PhantomMoveTargetGoal(final Phantom this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   protected boolean touchingTarget() {
      return this.this$0.moveTargetPoint.distanceToSqr(this.this$0.getX(), this.this$0.getY(), this.this$0.getZ()) < 4.0;
   }
}
