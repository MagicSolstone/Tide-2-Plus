package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap$Types;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Ghast$RandomFloatAroundGoal extends Goal {
   private static final int MAX_ATTEMPTS = 64;
   private final Mob ghast;
   private final int distanceToBlocks;

   public Ghast$RandomFloatAroundGoal(final Mob ghast) {
      this(ghast, 0);
   }

   public Ghast$RandomFloatAroundGoal(final Mob ghast, final int distanceToBlocks) {
      this.ghast = ghast;
      this.distanceToBlocks = distanceToBlocks;
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      MoveControl moveControl = this.ghast.getMoveControl();
      if (!moveControl.hasWanted()) {
         return true;
      } else {
         double xd = moveControl.getWantedX() - this.ghast.getX();
         double yd = moveControl.getWantedY() - this.ghast.getY();
         double zd = moveControl.getWantedZ() - this.ghast.getZ();
         double dd = xd * xd + yd * yd + zd * zd;
         return dd < 1.0 || dd > 3600.0;
      }
   }

   @Override
   public boolean canContinueToUse() {
      return false;
   }

   @Override
   public void start() {
      Vec3 result = getSuitableFlyToPosition(this.ghast, this.distanceToBlocks);
      this.ghast.getMoveControl().setWantedPosition(result.x(), result.y(), result.z(), 1.0);
   }

   public static Vec3 getSuitableFlyToPosition(final Mob mob, final int distanceToBlocks) {
      Level level = mob.level();
      RandomSource random = mob.getRandom();
      Vec3 center = mob.position();
      Vec3 result = null;

      for (int i = 0; i < 64; i++) {
         result = chooseRandomPositionWithRestriction(mob, center, random);
         if (result != null && isGoodTarget(level, result, distanceToBlocks)) {
            return result;
         }
      }

      if (result == null) {
         result = chooseRandomPosition(center, random);
      }

      BlockPos pos = BlockPos.containing(result);
      int heightY = level.getHeight(Heightmap$Types.MOTION_BLOCKING, pos.getX(), pos.getZ());
      if (heightY < pos.getY() && heightY > level.getMinY()) {
         result = new Vec3(result.x(), mob.getY() - Math.abs(mob.getY() - result.y()), result.z());
      }

      return result;
   }

   private static boolean isGoodTarget(final Level level, final Vec3 target, final int distanceToBlocks) {
      if (distanceToBlocks <= 0) {
         return true;
      } else {
         BlockPos pos = BlockPos.containing(target);
         if (!level.getBlockState(pos).isAir()) {
            return false;
         } else {
            for (Direction dir : Direction.values()) {
               for (int i = 1; i < distanceToBlocks; i++) {
                  BlockPos offset = pos.relative(dir, i);
                  if (!level.getBlockState(offset).isAir()) {
                     return true;
                  }
               }
            }

            return false;
         }
      }
   }

   private static Vec3 chooseRandomPosition(final Vec3 center, final RandomSource random) {
      double xTarget = center.x() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
      double yTarget = center.y() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
      double zTarget = center.z() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
      return new Vec3(xTarget, yTarget, zTarget);
   }

   @Nullable
   private static Vec3 chooseRandomPositionWithRestriction(final Mob mob, final Vec3 center, final RandomSource random) {
      Vec3 target = chooseRandomPosition(center, random);
      return mob.hasHome() && !mob.isWithinHome(target) ? null : target;
   }
}
