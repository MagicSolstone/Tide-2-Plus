package net.minecraft.world.waypoints;

public interface WaypointTransmitter$BlockConnection extends WaypointTransmitter$Connection {
   int distanceManhattan();

   @Override
   default boolean isBroken() {
      return this.distanceManhattan() > 1;
   }
}
