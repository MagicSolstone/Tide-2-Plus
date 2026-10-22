package net.minecraft.world.waypoints;

import com.mojang.datafixers.util.Either;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

class TrackedWaypoint$ChunkWaypoint extends TrackedWaypoint {
   private ChunkPos chunkPos;

   public TrackedWaypoint$ChunkWaypoint(final UUID identifier, final Waypoint$Icon icon, final ChunkPos chunkPos) {
      super(Either.left(identifier), icon, TrackedWaypoint$Type.CHUNK);
      this.chunkPos = chunkPos;
   }

   public TrackedWaypoint$ChunkWaypoint(final Either<UUID, String> identifier, final Waypoint$Icon icon, final FriendlyByteBuf byteBuf) {
      super(identifier, icon, TrackedWaypoint$Type.CHUNK);
      this.chunkPos = new ChunkPos(byteBuf.readVarInt(), byteBuf.readVarInt());
   }

   @Override
   public void update(final TrackedWaypoint other) {
      if (other instanceof TrackedWaypoint$ChunkWaypoint chunkWaypoint) {
         this.chunkPos = chunkWaypoint.chunkPos;
      } else {
         TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass());
      }
   }

   @Override
   public void writeContents(final ByteBuf buf) {
      VarInt.write(buf, this.chunkPos.x());
      VarInt.write(buf, this.chunkPos.z());
   }

   private Vec3 position(final double positionY) {
      return Vec3.atCenterOf(this.chunkPos.getMiddleBlockPosition((int)positionY));
   }

   @Override
   public double yawAngleToCamera(final Level level, final TrackedWaypoint$Camera camera, final PartialTickSupplier partialTickSupplier) {
      Vec3 cameraPosition = camera.position();
      Vec3 direction = cameraPosition.subtract(this.position(cameraPosition.y())).rotateClockwise90();
      float waypointAngle = (float)Mth.atan2(direction.z(), direction.x()) * (180.0F / (float)Math.PI);
      return Mth.degreesDifference(camera.yaw(), waypointAngle);
   }

   @Override
   public TrackedWaypoint$PitchDirection pitchDirectionToCamera(
      final Level level, final TrackedWaypoint$Projector projector, final PartialTickSupplier partialTickSupplier
   ) {
      double onScreenHorizon = projector.projectHorizonToScreen();
      if (onScreenHorizon < -1.0) {
         return TrackedWaypoint$PitchDirection.DOWN;
      } else {
         return onScreenHorizon > 1.0 ? TrackedWaypoint$PitchDirection.UP : TrackedWaypoint$PitchDirection.NONE;
      }
   }

   @Override
   public double distanceSquared(final Entity fromEntity) {
      return fromEntity.distanceToSqr(Vec3.atCenterOf(this.chunkPos.getMiddleBlockPosition(fromEntity.getBlockY())));
   }
}
