package net.minecraft.world.level;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.chunk.ChunkAccess;

@FunctionalInterface
public interface NaturalSpawner$AfterSpawnCallback {
   void run(final Mob mob, final ChunkAccess levelChunk);
}
