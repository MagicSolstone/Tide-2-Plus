package net.minecraft.world.waypoints;

import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;

public class WaypointTransmitter$EntityChunkConnection implements WaypointTransmitter$ChunkConnection {
   private final LivingEntity source;
   private final Waypoint$Icon icon;
   private final ServerPlayer receiver;
   private ChunkPos lastPosition;

   public WaypointTransmitter$EntityChunkConnection(final LivingEntity source, final Waypoint$Icon icon, final ServerPlayer receiver) {
      this.source = source;
      this.icon = icon;
      this.receiver = receiver;
      this.lastPosition = source.chunkPosition();
   }

   @Override
   public int distanceChessboard() {
      return this.lastPosition.getChessboardDistance(this.source.chunkPosition());
   }

   @Override
   public void connect() {
      this.receiver.connection.send(ClientboundTrackedWaypointPacket.addWaypointChunk(this.source.getUUID(), this.icon, this.lastPosition));
   }

   @Override
   public void disconnect() {
      this.receiver.connection.send(ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
   }

   @Override
   public void update() {
      ChunkPos currentPosition = this.source.chunkPosition();
      if (currentPosition.getChessboardDistance(this.lastPosition) > 0) {
         this.receiver.connection.send(ClientboundTrackedWaypointPacket.updateWaypointChunk(this.source.getUUID(), this.icon, currentPosition));
         this.lastPosition = currentPosition;
      }
   }

   @Override
   public boolean isBroken() {
      return !WaypointTransmitter$ChunkConnection.super.isBroken() && !WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver)
         ? WaypointTransmitter.isChunkVisible(this.lastPosition, this.receiver)
         : true;
   }
}
