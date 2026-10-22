package net.minecraft.util.debug;

import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiManager$Occupancy;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.ChunkPos;

public class TrackingDebugSynchronizer$VillageSectionSynchronizer extends TrackingDebugSynchronizer<Unit> {
   public TrackingDebugSynchronizer$VillageSectionSynchronizer() {
      super(DebugSubscriptions.VILLAGE_SECTIONS);
   }

   @Override
   protected void sendInitialChunk(final ServerPlayer player, final ChunkPos chunkPos) {
      ServerLevel level = player.level();
      PoiManager poiManager = level.getPoiManager();
      poiManager.getInChunk(t -> true, chunkPos, PoiManager$Occupancy.ANY).forEach(record -> {
         SectionPos centerSection = SectionPos.of(record.getPos());
         forEachVillageSectionUpdate(level, centerSection, (sectionPos, isVillage) -> {
            BlockPos sectionBlockPos = sectionPos.center();
            player.connection.send(new ClientboundDebugBlockValuePacket(sectionBlockPos, this.subscription.packUpdate(isVillage ? Unit.INSTANCE : null)));
         });
      });
   }

   public void onPoiAdded(final ServerLevel level, final PoiRecord record) {
      this.sendVillageSectionsPacket(level, record.getPos());
   }

   public void onPoiRemoved(final ServerLevel level, final BlockPos poiPos) {
      this.sendVillageSectionsPacket(level, poiPos);
   }

   private void sendVillageSectionsPacket(final ServerLevel level, final BlockPos poiPos) {
      forEachVillageSectionUpdate(
         level,
         SectionPos.of(poiPos),
         (sectionPos, isVillage) -> {
            BlockPos sectionBlockPos = sectionPos.center();
            if (isVillage) {
               this.sendToPlayersTrackingChunk(
                  level,
                  ChunkPos.containing(sectionBlockPos),
                  new ClientboundDebugBlockValuePacket(sectionBlockPos, this.subscription.packUpdate(Unit.INSTANCE))
               );
            } else {
               this.sendToPlayersTrackingChunk(
                  level, ChunkPos.containing(sectionBlockPos), new ClientboundDebugBlockValuePacket(sectionBlockPos, this.subscription.emptyUpdate())
               );
            }
         }
      );
   }

   private static void forEachVillageSectionUpdate(final ServerLevel level, final SectionPos centerSection, final BiConsumer<SectionPos, Boolean> consumer) {
      for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
         for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetY = -1; offsetY <= 1; offsetY++) {
               SectionPos sectionPos = centerSection.offset(offsetX, offsetY, offsetZ);
               if (level.isVillage(sectionPos.center())) {
                  consumer.accept(sectionPos, true);
               } else {
                  consumer.accept(sectionPos, false);
               }
            }
         }
      }
   }
}
