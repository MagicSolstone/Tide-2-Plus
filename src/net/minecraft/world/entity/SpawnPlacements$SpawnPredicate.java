package net.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;

@FunctionalInterface
public interface SpawnPlacements$SpawnPredicate<T extends Entity> {
   boolean test(EntityType<T> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random);
}
