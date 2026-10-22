package net.minecraft.world.entity.animal.happyghast;

import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

class HappyGhast$BabyFlyingPathNavigation extends FlyingPathNavigation {
   public HappyGhast$BabyFlyingPathNavigation(final HappyGhast mob, final Level level) {
      super(mob, level);
      this.setCanOpenDoors(false);
      this.setCanFloat(true);
      this.setRequiredPathLength(48.0F);
   }

   @Override
   protected boolean canMoveDirectly(final Vec3 startPos, final Vec3 stopPos) {
      return isClearForMovementBetween(this.mob, startPos, stopPos, false);
   }
}
