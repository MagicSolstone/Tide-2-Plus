package net.minecraft.world.entity.boss.wither;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

class WitherBoss$WitherDoNothingGoal extends Goal {
   public WitherBoss$WitherDoNothingGoal(final WitherBoss this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.setFlags(EnumSet.of(Goal$Flag.MOVE, Goal$Flag.JUMP, Goal$Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      return this.this$0.getInvulnerableTicks() > 0;
   }
}
