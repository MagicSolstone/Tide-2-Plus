package net.minecraft.world.waypoints;

import com.mojang.datafixers.util.Either;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.lang3.function.TriFunction;

enum TrackedWaypoint$Type {
   EMPTY(TrackedWaypoint$EmptyWaypoint::new),
   VEC3I(TrackedWaypoint$Vec3iWaypoint::new),
   CHUNK(TrackedWaypoint$ChunkWaypoint::new),
   AZIMUTH(TrackedWaypoint$AzimuthWaypoint::new);

   private final TriFunction<Either<UUID, String>, Waypoint$Icon, FriendlyByteBuf, TrackedWaypoint> constructor;

   private TrackedWaypoint$Type(final TriFunction<Either<UUID, String>, Waypoint$Icon, FriendlyByteBuf, TrackedWaypoint> constructor) {
      this.constructor = constructor;
   }
}
