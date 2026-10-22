package net.minecraft.world.waypoints;

public interface WaypointTransmitter$Connection {
   void connect();

   void disconnect();

   void update();

   boolean isBroken();
}
