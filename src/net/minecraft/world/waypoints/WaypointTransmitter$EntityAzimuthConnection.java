package net.minecraft.world.waypoints;

import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class WaypointTransmitter$EntityAzimuthConnection implements WaypointTransmitter$Connection {
   private final LivingEntity source;
   private final Waypoint$Icon icon;
   private final ServerPlayer receiver;
   private float lastAngle;

   public WaypointTransmitter$EntityAzimuthConnection(final LivingEntity source, final Waypoint$Icon icon, final ServerPlayer receiver) {
      this.source = source;
      this.icon = icon;
      this.receiver = receiver;
      Vec3 direction = receiver.position().subtract(source.position()).rotateClockwise90();
      this.lastAngle = (float)Mth.atan2(direction.z(), direction.x());
   }

   @Override
   public boolean isBroken() {
      return WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver)
         || WaypointTransmitter.isChunkVisible(this.source.chunkPosition(), this.receiver)
         || !WaypointTransmitter.isReallyFar(this.source, this.receiver);
   }

   @Override
   public void connect() {
      this.receiver.connection.send(ClientboundTrackedWaypointPacket.addWaypointAzimuth(this.source.getUUID(), this.icon, this.lastAngle));
   }

   @Override
   public void disconnect() {
      this.receiver.connection.send(ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
   }

   @Override
   public void update() {
      Vec3 direction = this.receiver.position().subtract(this.source.position()).rotateClockwise90();
      float currentAngle = (float)Mth.atan2(direction.z(), direction.x());
      if (Mth.abs(currentAngle - this.lastAngle) > 0.008726646F) {
         this.receiver.connection.send(ClientboundTrackedWaypointPacket.updateWaypointAzimuth(this.source.getUUID(), this.icon, currentAngle));
         this.lastAngle = currentAngle;
      }
   }
}
