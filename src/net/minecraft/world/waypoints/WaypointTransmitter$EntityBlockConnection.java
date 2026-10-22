package net.minecraft.world.waypoints;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class WaypointTransmitter$EntityBlockConnection implements WaypointTransmitter$BlockConnection {
   private final LivingEntity source;
   private final Waypoint$Icon icon;
   private final ServerPlayer receiver;
   private BlockPos lastPosition;

   public WaypointTransmitter$EntityBlockConnection(final LivingEntity source, final Waypoint$Icon icon, final ServerPlayer receiver) {
      this.source = source;
      this.receiver = receiver;
      this.icon = icon;
      this.lastPosition = source.blockPosition();
   }

   @Override
   public void connect() {
      this.receiver.connection.send(ClientboundTrackedWaypointPacket.addWaypointPosition(this.source.getUUID(), this.icon, this.lastPosition));
   }

   @Override
   public void disconnect() {
      this.receiver.connection.send(ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
   }

   @Override
   public void update() {
      BlockPos currentPosition = this.source.blockPosition();
      if (currentPosition.distManhattan(this.lastPosition) > 0) {
         this.receiver.connection.send(ClientboundTrackedWaypointPacket.updateWaypointPosition(this.source.getUUID(), this.icon, currentPosition));
         this.lastPosition = currentPosition;
      }
   }

   @Override
   public int distanceManhattan() {
      return this.lastPosition.distManhattan(this.source.blockPosition());
   }

   @Override
   public boolean isBroken() {
      return WaypointTransmitter$BlockConnection.super.isBroken() || WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver);
   }
}
