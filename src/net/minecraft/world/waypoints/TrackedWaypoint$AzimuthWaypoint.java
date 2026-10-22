package net.minecraft.world.waypoints;

import com.mojang.datafixers.util.Either;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

class TrackedWaypoint$AzimuthWaypoint extends TrackedWaypoint {
   private float angle;

   public TrackedWaypoint$AzimuthWaypoint(final UUID identifier, final Waypoint$Icon icon, final float angle) {
      super(Either.left(identifier), icon, TrackedWaypoint$Type.AZIMUTH);
      this.angle = angle;
   }

   public TrackedWaypoint$AzimuthWaypoint(final Either<UUID, String> identifier, final Waypoint$Icon icon, final FriendlyByteBuf byteBuf) {
      super(identifier, icon, TrackedWaypoint$Type.AZIMUTH);
      this.angle = byteBuf.readFloat();
   }

   @Override
   public void update(final TrackedWaypoint other) {
      if (other instanceof TrackedWaypoint$AzimuthWaypoint azimuthWaypoint) {
         this.angle = azimuthWaypoint.angle;
      } else {
         TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass());
      }
   }

   @Override
   public void writeContents(final ByteBuf buf) {
      buf.writeFloat(this.angle);
   }

   @Override
   public double yawAngleToCamera(final Level level, final TrackedWaypoint$Camera camera, final PartialTickSupplier partialTickSupplier) {
      return Mth.degreesDifference(camera.yaw(), this.angle * (180.0F / (float)Math.PI));
   }

   @Override
   public TrackedWaypoint$PitchDirection pitchDirectionToCamera(
      final Level level, final TrackedWaypoint$Projector projector, final PartialTickSupplier partialTickSupplier
   ) {
      double horizon = projector.projectHorizonToScreen();
      if (horizon < -1.0) {
         return TrackedWaypoint$PitchDirection.DOWN;
      } else {
         return horizon > 1.0 ? TrackedWaypoint$PitchDirection.UP : TrackedWaypoint$PitchDirection.NONE;
      }
   }

   @Override
   public double distanceSquared(final Entity fromEntity) {
      return Double.POSITIVE_INFINITY;
   }
}
