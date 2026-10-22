package net.minecraft.util;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;

public class SpawnUtil {
   public static <T extends Mob> Optional<T> trySpawnMob(
      final EntityType<T> entityType,
      final EntitySpawnReason spawnReason,
      final ServerLevel level,
      final BlockPos start,
      final int spawnAttempts,
      final int spawnRangeXZ,
      final int spawnRangeY,
      final SpawnUtil$Strategy strategy,
      final boolean checkCollisions
   ) {
      BlockPos$MutableBlockPos searchPos = start.mutable();
      RandomSource random = level.getRandom();

      for (int i = 0; i < spawnAttempts; i++) {
         int dx = Mth.randomBetweenInclusive(random, -spawnRangeXZ, spawnRangeXZ);
         int dz = Mth.randomBetweenInclusive(random, -spawnRangeXZ, spawnRangeXZ);
         searchPos.setWithOffset(start, dx, spawnRangeY, dz);
         if (level.getWorldBorder().isWithinBounds(searchPos)
            && moveToPossibleSpawnPosition(level, spawnRangeY, searchPos, strategy)
            && (!checkCollisions || level.noCollision(entityType.getSpawnAABB(searchPos.getX() + 0.5, searchPos.getY(), searchPos.getZ() + 0.5)))) {
            T mob = (T)entityType.create(level, null, searchPos, spawnReason, false, false);
            if (mob != null) {
               if (mob.checkSpawnRules(level, spawnReason) && mob.checkSpawnObstruction(level)) {
                  level.addFreshEntityWithPassengers(mob);
                  mob.playAmbientSound();
                  return Optional.of(mob);
               }

               mob.discard();
            }
         }
      }

      return Optional.empty();
   }

   private static boolean moveToPossibleSpawnPosition(
      final ServerLevel level, final int spawnRangeY, final BlockPos$MutableBlockPos searchPos, final SpawnUtil$Strategy strategy
   ) {
      BlockPos$MutableBlockPos abovePos = new BlockPos$MutableBlockPos().set(searchPos);
      BlockState aboveState = level.getBlockState(abovePos);

      for (int y = spawnRangeY; y >= -spawnRangeY; y--) {
         searchPos.move(Direction.DOWN);
         abovePos.setWithOffset(searchPos, Direction.UP);
         BlockState currentState = level.getBlockState(searchPos);
         if (strategy.canSpawnOn(level, searchPos, currentState, abovePos, aboveState)) {
            searchPos.move(Direction.UP);
            return true;
         }

         aboveState = currentState;
      }

      return false;
   }
}
