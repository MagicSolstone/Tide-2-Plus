package net.minecraft.world.entity.ai.goal;

import java.util.function.Predicate;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class TemptGoal$ForNonPathfinders extends TemptGoal {
   public TemptGoal$ForNonPathfinders(
      final Mob mob, final double speedModifier, final Predicate<ItemStack> items, final boolean canScare, final double stopDistance
   ) {
      super(mob, speedModifier, items, canScare, stopDistance);
   }

   @Override
   protected void stopNavigation() {
      this.mob.getMoveControl().setWait();
   }

   @Override
   protected void navigateTowards(final Player player) {
      Vec3 target = player.getEyePosition().subtract(this.mob.position()).scale(this.mob.getRandom().nextDouble()).add(this.mob.position());
      this.mob.getMoveControl().setWantedPosition(target.x, target.y, target.z, this.speedModifier);
   }
}
