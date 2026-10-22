package net.minecraft.server.level;

import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Objects;

class DistanceManager$FixedPlayerDistanceChunkTracker extends ChunkTracker {
   protected final Long2ByteMap chunks;
   protected final int maxDistance;

   protected DistanceManager$FixedPlayerDistanceChunkTracker(final DistanceManager this$0, final int maxDistance) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(maxDistance + 2, 16, 256);
      this.chunks = new Long2ByteOpenHashMap();
      this.maxDistance = maxDistance;
      this.chunks.defaultReturnValue((byte)(maxDistance + 2));
   }

   @Override
   protected int getLevel(final long node) {
      return this.chunks.get(node);
   }

   @Override
   protected void setLevel(final long node, final int level) {
      byte oldLevel;
      if (level > this.maxDistance) {
         oldLevel = this.chunks.remove(node);
      } else {
         oldLevel = this.chunks.put(node, (byte)level);
      }

      this.onLevelChange(node, oldLevel, level);
   }

   protected void onLevelChange(final long node, final int oldLevel, final int level) {
   }

   @Override
   protected int getLevelFromSource(final long to) {
      return this.havePlayer(to) ? 0 : Integer.MAX_VALUE;
   }

   private boolean havePlayer(final long chunkPos) {
      ObjectSet<ServerPlayer> players = (ObjectSet<ServerPlayer>)this.this$0.playersPerChunk.get(chunkPos);
      return players != null && !players.isEmpty();
   }

   public void runAllUpdates() {
      this.runUpdates(Integer.MAX_VALUE);
   }
}
