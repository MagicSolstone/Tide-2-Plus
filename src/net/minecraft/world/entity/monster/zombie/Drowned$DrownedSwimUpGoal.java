package net.minecraft.world.entity.monster.zombie;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

class Drowned$DrownedSwimUpGoal extends Goal {
   private final Drowned drowned;
   private final double speedModifier;
   private final int seaLevel;
   private boolean stuck;

   public Drowned$DrownedSwimUpGoal(final Drowned drowned, final double speedModifier, final int seaLevel) {
      this.drowned = drowned;
      this.speedModifier = speedModifier;
      this.seaLevel = seaLevel;
   }

   @Override
   public boolean canUse() {
      return !this.drowned.level().isBrightOutside() && this.drowned.isInWater() && this.drowned.getY() < this.seaLevel - 2;
   }

   @Override
   public boolean canContinueToUse() {
      return this.canUse() && !this.stuck;
   }

   @Override
   public void tick() {
      if (this.drowned.getY() < this.seaLevel - 1 && (this.drowned.getNavigation().isDone() || this.drowned.closeToNextPos())) {
         Vec3 nextPos = DefaultRandomPos.getPosTowards(
            this.drowned, 4, 8, new Vec3(this.drowned.getX(), this.seaLevel - 1, this.drowned.getZ()), (float) (Math.PI / 2)
         );
         if (nextPos == null) {
            this.stuck = true;
            return;
         }

         this.drowned.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
      }
   }

   @Override
   public void start() {
      this.drowned.setSearchingForLand(true);
      this.stuck = false;
   }

   @Override
   public void stop() {
      this.drowned.setSearchingForLand(false);
   }
}
