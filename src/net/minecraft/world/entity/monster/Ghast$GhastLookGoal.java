package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

public class Ghast$GhastLookGoal extends Goal {
   private final Mob ghast;

   public Ghast$GhastLookGoal(final Mob ghast) {
      this.ghast = ghast;
      this.setFlags(EnumSet.of(Goal$Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      return true;
   }

   @Override
   public boolean requiresUpdateEveryTick() {
      return true;
   }

   @Override
   public void tick() {
      Ghast.faceMovementDirection(this.ghast);
   }
}
