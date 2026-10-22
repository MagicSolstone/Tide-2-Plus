package net.minecraft.util.profiling.jfr;

import com.mojang.logging.LogUtils;
import java.net.SocketAddress;
import java.nio.file.Path;
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
import org.slf4j.Logger;

public class JvmProfiler$NoOpProfiler implements JvmProfiler {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final ProfiledDuration NO_OP_COMMIT = ignored -> {};

   @Override
   public boolean start(final Environment environment) {
      LOGGER.warn("Attempted to start Flight Recorder, but it's not supported on this JVM");
      return false;
   }

   @Override
   public Path stop() {
      throw new IllegalStateException("Attempted to stop Flight Recorder, but it's not supported on this JVM");
   }

   @Override
   public boolean isRunning() {
      return false;
   }

   @Override
   public boolean isAvailable() {
      return false;
   }

   @Override
   public void onPacketReceived(final ConnectionProtocol protocol, final PacketType<?> packetId, final SocketAddress remoteAddress, final int readableBytes) {
   }

   @Override
   public void onPacketSent(final ConnectionProtocol protocol, final PacketType<?> packetId, final SocketAddress remoteAddress, final int writtenBytes) {
   }

   @Override
   public void onRegionFileRead(final RegionStorageInfo info, final ChunkPos pos, final RegionFileVersion version, final int readBytes) {
   }

   @Override
   public void onRegionFileWrite(final RegionStorageInfo info, final ChunkPos pos, final RegionFileVersion version, final int writtenBytes) {
   }

   @Override
   public void onServerTick(final float averageTickTime) {
   }

   @Override
   public void onClientTick(final int fps) {
   }

   @Override
   public ProfiledDuration onWorldLoadedStarted() {
      return NO_OP_COMMIT;
   }

   @Nullable
   @Override
   public ProfiledDuration onChunkGenerate(final ChunkPos pos, final ResourceKey<Level> dimension, final String name) {
      return null;
   }

   @Override
   public ProfiledDuration onStructureGenerate(final ChunkPos sourceChunkPos, final ResourceKey<Level> dimension, final Holder<Structure> structure) {
      return NO_OP_COMMIT;
   }
}
