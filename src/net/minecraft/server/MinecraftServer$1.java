package net.minecraft.server;

import java.util.Objects;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkLoadStatusView;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.jspecify.annotations.Nullable;

class MinecraftServer$1 implements ChunkLoadStatusView {
   @Nullable
   private ChunkMap chunkMap;
   private int centerChunkX;
   private int centerChunkZ;

   MinecraftServer$1(final MinecraftServer this$0, final int val$radius) {
      this.val$radius = val$radiusx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void moveTo(final ResourceKey<Level> dimension, final ChunkPos centerChunk) {
      ServerLevel level = this.this$0.getLevel(dimension);
      this.chunkMap = level != null ? level.getChunkSource().chunkMap : null;
      this.centerChunkX = centerChunk.x();
      this.centerChunkZ = centerChunk.z();
   }

   @Nullable
   @Override
   public ChunkStatus get(final int x, final int z) {
      return this.chunkMap == null
         ? null
         : this.chunkMap.getLatestStatus(ChunkPos.pack(x + this.centerChunkX - this.val$radius, z + this.centerChunkZ - this.val$radius));
   }

   @Override
   public int radius() {
      return this.val$radius;
   }
}
