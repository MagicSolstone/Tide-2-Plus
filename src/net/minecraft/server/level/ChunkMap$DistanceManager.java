package net.minecraft.server.level;

import java.util.Objects;
import java.util.concurrent.Executor;
import net.minecraft.world.level.TicketStorage;
import org.jspecify.annotations.Nullable;

class ChunkMap$DistanceManager extends DistanceManager {
   protected ChunkMap$DistanceManager(final ChunkMap this$0, final TicketStorage ticketStorage, final Executor executor, final Executor mainThreadExecutor) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(ticketStorage, executor, mainThreadExecutor);
   }

   @Override
   protected boolean isChunkToRemove(final long node) {
      return this.this$0.toDrop.contains(node);
   }

   @Nullable
   @Override
   protected ChunkHolder getChunk(final long node) {
      return this.this$0.getUpdatingChunkIfPresent(node);
   }

   @Nullable
   @Override
   protected ChunkHolder updateChunkScheduling(final long node, final int level, @Nullable final ChunkHolder chunk, final int oldLevel) {
      return this.this$0.updateChunkScheduling(node, level, chunk, oldLevel);
   }
}
