package net.minecraft.world.waypoints;

import java.util.Optional;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ChunkPos;

public interface WaypointTransmitter extends Waypoint {
   int REALLY_FAR_DISTANCE = 332;

   boolean isTransmittingWaypoint();

   Optional<WaypointTransmitter$Connection> makeWaypointConnectionWith(ServerPlayer player);

   Waypoint$Icon waypointIcon();

   static boolean doesSourceIgnoreReceiver(final LivingEntity source, final ServerPlayer receiver) {
      if (receiver.isSpectator()) {
         return false;
      } else if (!source.isSpectator() && !source.hasIndirectPassenger(receiver)) {
         double broadcastRange = Math.min(
            source.getAttributeValue(Attributes.WAYPOINT_TRANSMIT_RANGE), receiver.getAttributeValue(Attributes.WAYPOINT_RECEIVE_RANGE)
         );
         return source.distanceTo(receiver) >= broadcastRange;
      } else {
         return true;
      }
   }

   static boolean isChunkVisible(final ChunkPos chunkPos, final ServerPlayer receiver) {
      return receiver.getChunkTrackingView().isInViewDistance(chunkPos.x(), chunkPos.z());
   }

   static boolean isReallyFar(final LivingEntity source, final ServerPlayer receiver) {
      return source.distanceTo(receiver) > 332.0F;
   }
}
