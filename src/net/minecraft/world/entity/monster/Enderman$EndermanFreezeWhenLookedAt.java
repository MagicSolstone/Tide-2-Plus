package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

class Enderman$EndermanFreezeWhenLookedAt extends Goal {
   private final Enderman enderman;
   @Nullable
   private LivingEntity target;

   public Enderman$EndermanFreezeWhenLookedAt(final Enderman enderman) {
      this.enderman = enderman;
      this.setFlags(EnumSet.of(Goal$Flag.JUMP, Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      this.target = this.enderman.getTarget();
      if (this.target instanceof Player playerTarget) {
         double dist = this.target.distanceToSqr(this.enderman);
         return dist > 256.0 ? false : this.enderman.isBeingStaredBy(playerTarget);
      } else {
         return false;
      }
   }

   @Override
   public void start() {
      this.enderman.getNavigation().stop();
   }

   @Override
   public void tick() {
      this.enderman.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
   }
}
