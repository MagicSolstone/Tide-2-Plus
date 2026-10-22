package net.minecraft.util.profiling.jfr;

import java.net.SocketAddress;
import java.nio.file.Path;
import jdk.jfr.FlightRecorder;
import net.minecraft.core.Holder;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.jfr.callback.ProfiledDuration;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.storage.RegionFileVersion;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jspecify.annotations.Nullable;

public interface JvmProfiler {
   JvmProfiler INSTANCE = (JvmProfiler)(Runtime.class.getModule().getLayer().findModule("jdk.jfr").isPresent() && FlightRecorder.isAvailable()
      ? JfrProfiler.getInstance()
      : new JvmProfiler$NoOpProfiler());

   boolean start(Environment environment);

   Path stop();

   boolean isRunning();

   boolean isAvailable();

   void onServerTick(float averageTickTime);

   void onClientTick(int fps);

   void onPacketReceived(final ConnectionProtocol protocol, final PacketType<?> packetId, final SocketAddress remoteAddress, final int readableBytes);

   void onPacketSent(final ConnectionProtocol protocol, final PacketType<?> packetId, final SocketAddress remoteAddress, final int writtenBytes);

   void onRegionFileRead(RegionStorageInfo info, ChunkPos pos, RegionFileVersion version, int readBytes);

   void onRegionFileWrite(RegionStorageInfo info, ChunkPos pos, RegionFileVersion version, int writtenBytes);

   @Nullable
   ProfiledDuration onWorldLoadedStarted();

   @Nullable
   ProfiledDuration onChunkGenerate(ChunkPos pos, ResourceKey<Level> dimension, String name);

   @Nullable
   ProfiledDuration onStructureGenerate(ChunkPos sourceChunkPos, ResourceKey<Level> dimension, Holder<Structure> structure);
}
