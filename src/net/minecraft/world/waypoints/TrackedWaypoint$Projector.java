package net.minecraft.world.waypoints;

import net.minecraft.world.phys.Vec3;

public interface TrackedWaypoint$Projector {
   Vec3 projectPointToScreen(final Vec3 point);

   double projectHorizonToScreen();
}
