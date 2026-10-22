package net.minecraft.world.entity.monster.cubemob;

import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.ai.goal.TemptGoal$ForNonPathfinders;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

class SulfurCube$SulfurCubeTemptGoal extends TemptGoal$ForNonPathfinders {
   public SulfurCube$SulfurCubeTemptGoal(
      final Mob mob, final double speedModifier, final Predicate<ItemStack> items, final boolean canScare, final double stopDistance
   ) {
      super(mob, speedModifier, items, canScare, stopDistance);
      this.setFlags(EnumSet.of(Goal$Flag.LOOK));
   }

   @Override
   protected void stopNavigation() {
      if (this.mob.getMoveControl() instanceof AbstractCubeMob$CubeMobMoveControl cubeMobMoveControl) {
         cubeMobMoveControl.setWantedMovement(0.0);
      }
   }

   @Override
   protected void navigateTowards(final Player player) {
      this.mob.lookAt(player, 10.0F, 10.0F);
      if (this.mob.getMoveControl() instanceof AbstractCubeMob$CubeMobMoveControl cubeMobMoveControl) {
         cubeMobMoveControl.setDirection(this.mob.getYRot(), true);
      }
   }
}
