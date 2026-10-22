package net.minecraft.server.network.config;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkLoadCounter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.server.level.progress.LevelLoadListener$Stage;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

final class PrepareSpawnTask$Preparing implements PrepareSpawnTask$State {
   private final ServerLevel spawnLevel;
   private final CompletableFuture<Vec3> spawnPosition;
   private final Vec2 spawnAngle;
   @Nullable
   private CompletableFuture<?> chunkLoadFuture;
   private final ChunkLoadCounter chunkLoadCounter;

   private PrepareSpawnTask$Preparing(final ServerLevel this$0, final CompletableFuture<Vec3> spawnLevel, final Vec2 spawnPosition, final Vec2 spawnAngle) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.chunkLoadCounter = new ChunkLoadCounter();
      this.spawnLevel = spawnLevel;
      this.spawnPosition = spawnPosition;
      this.spawnAngle = spawnAngle;
   }

   public void cancel() {
      this.spawnPosition.cancel(false);
   }

   public PrepareSpawnTask$Ready tick() {
      if (!this.spawnPosition.isDone()) {
         return null;
      } else {
         Vec3 spawnPosition = this.spawnPosition.join();
         if (this.chunkLoadFuture == null) {
            ChunkPos spawnChunk = ChunkPos.containing(BlockPos.containing(spawnPosition));
            this.chunkLoadCounter
               .track(
                  this.spawnLevel,
                  () -> this.chunkLoadFuture = this.spawnLevel.getChunkSource().addTicketAndLoadWithRadius(TicketType.PLAYER_SPAWN, spawnChunk, 3)
               );
            this.this$0.loadListener.start(LevelLoadListener$Stage.LOAD_PLAYER_CHUNKS, this.chunkLoadCounter.totalChunks());
            this.this$0.loadListener.updateFocus(this.spawnLevel.dimension(), spawnChunk);
         }

         this.this$0.loadListener.update(LevelLoadListener$Stage.LOAD_PLAYER_CHUNKS, this.chunkLoadCounter.readyChunks(), this.chunkLoadCounter.totalChunks());
         if (!this.chunkLoadFuture.isDone()) {
            return null;
         } else {
            this.this$0.loadListener.finish(LevelLoadListener$Stage.LOAD_PLAYER_CHUNKS);
            return new PrepareSpawnTask$Ready(this.this$0, this.spawnLevel, spawnPosition, this.spawnAngle);
         }
      }
   }
}
