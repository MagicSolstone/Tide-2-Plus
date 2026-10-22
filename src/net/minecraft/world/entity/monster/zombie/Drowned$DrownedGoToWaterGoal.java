package net.minecraft.world.entity.monster.zombie;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

class Drowned$DrownedGoToWaterGoal extends Goal {
   private final PathfinderMob mob;
   private double wantedX;
   private double wantedY;
   private double wantedZ;
   private final double speedModifier;
   private final Level level;

   public Drowned$DrownedGoToWaterGoal(final PathfinderMob mob, final double speedModifier) {
      this.mob = mob;
      this.speedModifier = speedModifier;
      this.level = mob.level();
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      if (!this.level.isBrightOutside()) {
         return false;
      } else if (this.mob.isInWater()) {
         return false;
      } else {
         Vec3 pos = this.getWaterPos();
         if (pos == null) {
            return false;
         } else {
            this.wantedX = pos.x;
            this.wantedY = pos.y;
            this.wantedZ = pos.z;
            return true;
         }
      }
   }

   @Override
   public boolean canContinueToUse() {
      return !this.mob.getNavigation().isDone();
   }

   @Override
   public void start() {
      this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
   }

   @Nullable
   private Vec3 getWaterPos() {
      RandomSource random = this.mob.getRandom();
      BlockPos pos = this.mob.blockPosition();

      for (int i = 0; i < 10; i++) {
         BlockPos randomPos = pos.offset(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);
         if (this.level.getBlockState(randomPos).is(Blocks.WATER)) {
            return Vec3.atBottomCenterOf(randomPos);
         }
      }

      return null;
   }
}
