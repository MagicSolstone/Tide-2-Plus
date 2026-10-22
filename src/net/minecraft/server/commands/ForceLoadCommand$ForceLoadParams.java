package net.minecraft.server.commands;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

record ForceLoadCommand$ForceLoadParams(ResourceKey<Level> dimension, int minChunkX, int minChunkZ, int maxChunkX, int maxChunkZ) {
   public ChunkPos min() {
      return new ChunkPos(this.minChunkX, this.minChunkZ);
   }

   public ChunkPos max() {
      return new ChunkPos(this.maxChunkX, this.maxChunkZ);
   }
}
