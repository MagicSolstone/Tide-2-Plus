package net.minecraft.server.level;

import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntMaps;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.Long2ByteMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Objects;
import net.minecraft.world.level.ChunkPos;

class DistanceManager$PlayerTicketTracker extends DistanceManager$FixedPlayerDistanceChunkTracker {
   private int viewDistance;
   private final Long2IntMap queueLevels;
   private final LongSet toUpdate;

   protected DistanceManager$PlayerTicketTracker(final DistanceManager this$0, final int maxDistance) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x, maxDistance);
      this.queueLevels = Long2IntMaps.synchronize(new Long2IntOpenHashMap());
      this.toUpdate = new LongOpenHashSet();
      this.viewDistance = 0;
      this.queueLevels.defaultReturnValue(maxDistance + 2);
   }

   @Override
   protected void onLevelChange(final long node, final int oldLevel, final int level) {
      this.toUpdate.add(node);
   }

   public void updateViewDistance(final int viewDistance) {
      ObjectIterator var2 = this.chunks.long2ByteEntrySet().iterator();

      while (var2.hasNext()) {
         Entry entry = (Entry)var2.next();
         byte level = entry.getByteValue();
         long key = entry.getLongKey();
         this.onLevelChange(key, level, this.haveTicketFor(level), level <= viewDistance);
      }

      this.viewDistance = viewDistance;
   }

   private void onLevelChange(final long key, final int level, final boolean saw, final boolean sees) {
      if (saw != sees) {
         Ticket ticket = new Ticket(TicketType.PLAYER_LOADING, DistanceManager.PLAYER_TICKET_LEVEL);
         if (sees) {
            this.this$0.ticketDispatcher.submit(() -> this.this$0.mainThreadExecutor.execute(() -> {
               if (this.haveTicketFor(this.getLevel(key))) {
                  this.this$0.ticketStorage.addTicket(key, ticket);
                  this.this$0.ticketsToRelease.add(key);
               } else {
                  this.this$0.ticketDispatcher.release(key, () -> {}, false);
               }
            }), key, () -> level);
         } else {
            this.this$0
               .ticketDispatcher
               .release(key, () -> this.this$0.mainThreadExecutor.execute(() -> this.this$0.ticketStorage.removeTicket(key, ticket)), true);
         }
      }
   }

   @Override
   public void runAllUpdates() {
      super.runAllUpdates();
      if (!this.toUpdate.isEmpty()) {
         LongIterator iterator = this.toUpdate.iterator();

         while (iterator.hasNext()) {
            long node = iterator.nextLong();
            int oldLevel = this.queueLevels.get(node);
            int level = this.getLevel(node);
            if (oldLevel != level) {
               this.this$0.ticketDispatcher.onLevelChange(ChunkPos.unpack(node), () -> this.queueLevels.get(node), level, l -> {
                  if (l >= this.queueLevels.defaultReturnValue()) {
                     this.queueLevels.remove(node);
                  } else {
                     this.queueLevels.put(node, l);
                  }
               });
               this.onLevelChange(node, level, this.haveTicketFor(oldLevel), this.haveTicketFor(level));
            }
         }

         this.toUpdate.clear();
      }
   }

   private boolean haveTicketFor(final int level) {
      return level <= this.viewDistance;
   }
}
