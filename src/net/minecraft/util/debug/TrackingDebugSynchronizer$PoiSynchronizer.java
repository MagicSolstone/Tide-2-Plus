package net.minecraft.util.debug;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiManager$Occupancy;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.ChunkPos;

public class TrackingDebugSynchronizer$PoiSynchronizer extends TrackingDebugSynchronizer<DebugPoiInfo> {
   public TrackingDebugSynchronizer$PoiSynchronizer() {
      super(DebugSubscriptions.POIS);
   }

   @Override
   protected void sendInitialChunk(final ServerPlayer player, final ChunkPos chunkPos) {
      ServerLevel level = player.level();
      PoiManager poiManager = level.getPoiManager();
      poiManager.getInChunk(t -> true, chunkPos, PoiManager$Occupancy.ANY)
         .forEach(
            record -> player.connection.send(new ClientboundDebugBlockValuePacket(record.getPos(), this.subscription.packUpdate(new DebugPoiInfo(record))))
         );
   }

   public void onPoiAdded(final ServerLevel level, final PoiRecord record) {
      this.sendToPlayersTrackingChunk(
         level,
         ChunkPos.containing(record.getPos()),
         new ClientboundDebugBlockValuePacket(record.getPos(), this.subscription.packUpdate(new DebugPoiInfo(record)))
      );
   }

   public void onPoiRemoved(final ServerLevel level, final BlockPos poiPos) {
      this.sendToPlayersTrackingChunk(level, ChunkPos.containing(poiPos), new ClientboundDebugBlockValuePacket(poiPos, this.subscription.emptyUpdate()));
   }

   public void onPoiTicketCountChanged(final ServerLevel level, final BlockPos poiPos) {
      this.sendToPlayersTrackingChunk(
         level,
         ChunkPos.containing(poiPos),
         new ClientboundDebugBlockValuePacket(poiPos, this.subscription.packUpdate(level.getPoiManager().getDebugPoiInfo(poiPos)))
      );
   }
}
