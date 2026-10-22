package net.minecraft.world.entity.monster.illager;

import java.util.EnumSet;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

class Vindicator$VindicatorBreakDoorGoal extends BreakDoorGoal {
   public Vindicator$VindicatorBreakDoorGoal(final Mob mob) {
      super(mob, 6, Vindicator.DOOR_BREAKING_PREDICATE);
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canContinueToUse() {
      Vindicator vindicator = (Vindicator)this.mob;
      return vindicator.hasActiveRaid() && super.canContinueToUse();
   }

   @Override
   public boolean canUse() {
      Vindicator vindicator = (Vindicator)this.mob;
      return vindicator.hasActiveRaid() && Vindicator.access$000(vindicator).nextInt(reducedTickDelay(10)) == 0 && super.canUse();
   }

   @Override
   public void start() {
      super.start();
      this.mob.setNoActionTime(0);
   }
}
