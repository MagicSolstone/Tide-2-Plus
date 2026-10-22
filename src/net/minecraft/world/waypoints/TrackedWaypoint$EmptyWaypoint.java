package net.minecraft.world.waypoints;

import com.mojang.datafixers.util.Either;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

class TrackedWaypoint$EmptyWaypoint extends TrackedWaypoint {
   private TrackedWaypoint$EmptyWaypoint(final Either<UUID, String> identifier, final Waypoint$Icon icon, final FriendlyByteBuf byteBuf) {
      super(identifier, icon, TrackedWaypoint$Type.EMPTY);
   }

   private TrackedWaypoint$EmptyWaypoint(final UUID identifier) {
      super(Either.left(identifier), Waypoint$Icon.NULL, TrackedWaypoint$Type.EMPTY);
   }

   @Override
   public void update(final TrackedWaypoint other) {
   }

   @Override
   public void writeContents(final ByteBuf buf) {
   }

   @Override
   public double yawAngleToCamera(final Level level, final TrackedWaypoint$Camera camera, final PartialTickSupplier partialTickSupplier) {
      return Double.NaN;
   }

   @Override
   public TrackedWaypoint$PitchDirection pitchDirectionToCamera(
      final Level level, final TrackedWaypoint$Projector projector, final PartialTickSupplier partialTickSupplier
   ) {
      return TrackedWaypoint$PitchDirection.NONE;
   }

   @Override
   public double distanceSquared(final Entity fromEntity) {
      return Double.POSITIVE_INFINITY;
   }
}
