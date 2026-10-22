package net.minecraft.world.entity.animal.squid;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

class Squid$SquidRandomMovementGoal extends Goal {
   private final Squid squid;

   public Squid$SquidRandomMovementGoal(final Squid squid) {
      this.squid = squid;
   }

   @Override
   public boolean canUse() {
      return true;
   }

   @Override
   public void tick() {
      int noActionTime = this.squid.getNoActionTime();
      if (noActionTime > 100) {
         this.squid.movementVector = Vec3.ZERO;
      } else if (this.squid.getRandom().nextInt(reducedTickDelay(50)) == 0 || !Squid.access$000(this.squid) || !this.squid.hasMovementVector()) {
         float angle = this.squid.getRandom().nextFloat() * (float) (Math.PI * 2);
         this.squid.movementVector = new Vec3(Mth.cos(angle) * 0.2F, -0.1F + this.squid.getRandom().nextFloat() * 0.2F, Mth.sin(angle) * 0.2F);
      }
   }
}
