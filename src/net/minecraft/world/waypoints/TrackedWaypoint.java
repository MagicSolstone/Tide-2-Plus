package net.minecraft.world.waypoints;

import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

public abstract class TrackedWaypoint implements Waypoint {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final StreamCodec<ByteBuf, TrackedWaypoint> STREAM_CODEC = StreamCodec.ofMember(TrackedWaypoint::write, TrackedWaypoint::read);
   protected final Either<UUID, String> identifier;
   private final Waypoint$Icon icon;
   private final TrackedWaypoint$Type type;

   private TrackedWaypoint(final Either<UUID, String> identifier, final Waypoint$Icon icon, final TrackedWaypoint$Type type) {
      this.identifier = identifier;
      this.icon = icon;
      this.type = type;
   }

   public Either<UUID, String> id() {
      return this.identifier;
   }

   public abstract void update(final TrackedWaypoint other);

   public void write(final ByteBuf buf) {
      FriendlyByteBuf byteBuf = new FriendlyByteBuf(buf);
      byteBuf.writeEither(this.identifier, UUIDUtil.STREAM_CODEC, FriendlyByteBuf::writeUtf);
      Waypoint$Icon.STREAM_CODEC.encode(byteBuf, this.icon);
      byteBuf.writeEnum(this.type);
      this.writeContents(buf);
   }

   public abstract void writeContents(final ByteBuf buf);

   private static TrackedWaypoint read(final ByteBuf buf) {
      FriendlyByteBuf byteBuf = new FriendlyByteBuf(buf);
      Either<UUID, String> identifier = byteBuf.readEither(UUIDUtil.STREAM_CODEC, FriendlyByteBuf::readUtf);
      Waypoint$Icon icon = Waypoint$Icon.STREAM_CODEC.decode(byteBuf);
      TrackedWaypoint$Type type = byteBuf.readEnum(TrackedWaypoint$Type.class);
      return (TrackedWaypoint)type.constructor.apply(identifier, icon, byteBuf);
   }

   public static TrackedWaypoint setPosition(final UUID identifier, final Waypoint$Icon icon, final Vec3i position) {
      return new TrackedWaypoint$Vec3iWaypoint(identifier, icon, position);
   }

   public static TrackedWaypoint setChunk(final UUID identifier, final Waypoint$Icon icon, final ChunkPos chunk) {
      return new TrackedWaypoint$ChunkWaypoint(identifier, icon, chunk);
   }

   public static TrackedWaypoint setAzimuth(final UUID identifier, final Waypoint$Icon icon, final float angle) {
      return new TrackedWaypoint$AzimuthWaypoint(identifier, icon, angle);
   }

   public static TrackedWaypoint empty(final UUID identifier) {
      return new TrackedWaypoint$EmptyWaypoint(identifier);
   }

   public abstract double yawAngleToCamera(final Level level, final TrackedWaypoint$Camera camera, final PartialTickSupplier partialTickSupplier);

   public abstract TrackedWaypoint$PitchDirection pitchDirectionToCamera(
      Level level, TrackedWaypoint$Projector projector, final PartialTickSupplier partialTickSupplier
   );

   public abstract double distanceSquared(final Entity fromEntity);

   public Waypoint$Icon icon() {
      return this.icon;
   }
}
