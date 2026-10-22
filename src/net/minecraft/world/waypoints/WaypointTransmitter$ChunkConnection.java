package net.minecraft.world.waypoints;

public interface WaypointTransmitter$ChunkConnection extends WaypointTransmitter$Connection {
   int distanceChessboard();

   @Override
   default boolean isBroken() {
      return this.distanceChessboard() > 1;
   }
}
