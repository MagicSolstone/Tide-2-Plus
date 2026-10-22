package net.minecraft.network.protocol.game;

import io.netty.buffer.ByteBuf;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.world.waypoints.TrackedWaypoint;
import net.minecraft.world.waypoints.TrackedWaypointManager;
import net.minecraft.world.waypoints.WaypointManager;

enum ClientboundTrackedWaypointPacket$Operation {
   TRACK(WaypointManager::trackWaypoint),
   UNTRACK(WaypointManager::untrackWaypoint),
   UPDATE(WaypointManager::updateWaypoint);

   private final BiConsumer<TrackedWaypointManager, TrackedWaypoint> action;
   public static final IntFunction<ClientboundTrackedWaypointPacket$Operation> BY_ID = ByIdMap.continuous(
      Enum::ordinal, values(), ByIdMap$OutOfBoundsStrategy.WRAP
   );
   public static final StreamCodec<ByteBuf, ClientboundTrackedWaypointPacket$Operation> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);

   private ClientboundTrackedWaypointPacket$Operation(final BiConsumer<TrackedWaypointManager, TrackedWaypoint> action) {
      this.action = action;
   }
}
