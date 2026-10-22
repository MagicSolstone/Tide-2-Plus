package net.minecraft.world.waypoints;

import com.mojang.datafixers.util.Either;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

class TrackedWaypoint$Vec3iWaypoint extends TrackedWaypoint {
   private Vec3i vector;

   public TrackedWaypoint$Vec3iWaypoint(final UUID identifier, final Waypoint$Icon icon, final Vec3i vector) {
      super(Either.left(identifier), icon, TrackedWaypoint$Type.VEC3I);
      this.vector = vector;
   }

   public TrackedWaypoint$Vec3iWaypoint(final Either<UUID, String> identifier, final Waypoint$Icon icon, final FriendlyByteBuf byteBuf) {
      super(identifier, icon, TrackedWaypoint$Type.VEC3I);
      this.vector = new Vec3i(byteBuf.readVarInt(), byteBuf.readVarInt(), byteBuf.readVarInt());
   }

   @Override
   public void update(final TrackedWaypoint other) {
      if (other instanceof TrackedWaypoint$Vec3iWaypoint vec3iWaypoint) {
         this.vector = vec3iWaypoint.vector;
      } else {
         TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass());
      }
   }

   @Override
   public void writeContents(final ByteBuf buf) {
      VarInt.write(buf, this.vector.getX());
      VarInt.write(buf, this.vector.getY());
      VarInt.write(buf, this.vector.getZ());
   }

   private Vec3 position(final Level level, final PartialTickSupplier partialTick) {
      return this.identifier
         .left()
         .map(level::getEntity)
         .map(e -> e.blockPosition().distManhattan(this.vector) > 3 ? null : e.getEyePosition(partialTick.apply(e)))
         .orElseGet(() -> Vec3.atCenterOf(this.vector));
   }

   @Override
   public double yawAngleToCamera(final Level level, final TrackedWaypoint$Camera camera, final PartialTickSupplier partialTickSupplier) {
      Vec3 direction = camera.position().subtract(this.position(level, partialTickSupplier)).rotateClockwise90();
      float waypointAngle = (float)Mth.atan2(direction.z(), direction.x()) * (180.0F / (float)Math.PI);
      return Mth.degreesDifference(camera.yaw(), waypointAngle);
   }

   @Override
   public TrackedWaypoint$PitchDirection pitchDirectionToCamera(
      final Level level, final TrackedWaypoint$Projector projector, final PartialTickSupplier partialTickSupplier
   ) {
      Vec3 pointOnScreen = projector.projectPointToScreen(this.position(level, partialTickSupplier));
      boolean isBehindCamera = pointOnScreen.z > 1.0;
      double yInFrontOfCamera = isBehindCamera ? -pointOnScreen.y : pointOnScreen.y;
      if (yInFrontOfCamera < -1.0) {
         return TrackedWaypoint$PitchDirection.DOWN;
      } else if (yInFrontOfCamera > 1.0) {
         return TrackedWaypoint$PitchDirection.UP;
      } else {
         if (isBehindCamera) {
            if (pointOnScreen.y > 0.0) {
               return TrackedWaypoint$PitchDirection.UP;
            }

            if (pointOnScreen.y < 0.0) {
               return TrackedWaypoint$PitchDirection.DOWN;
            }
         }

         return TrackedWaypoint$PitchDirection.NONE;
      }
   }

   @Override
   public double distanceSquared(final Entity fromEntity) {
      return fromEntity.distanceToSqr(Vec3.atCenterOf(this.vector));
   }
}
