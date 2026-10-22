package net.minecraft.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.chunk.ChunkAccess;

@FunctionalInterface
public interface NaturalSpawner$SpawnPredicate {
   boolean test(final EntityType<?> type, final ServerLevel level, final BlockPos blockPos, final ChunkAccess levelChunk);
}
