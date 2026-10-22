package net.minecraft.util.profiling.jfr.event;

import jdk.jfr.Category;
import jdk.jfr.Enabled;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;
import jdk.jfr.StackTrace;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.storage.RegionFileVersion;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;

@Category({"Minecraft", "Storage"})
@StackTrace(false)
@Enabled(false)
public abstract class ChunkRegionIoEvent extends Event {
   @Name("regionPosX")
   @Label("Region X Position")
   public final int regionPosX;
   @Name("regionPosZ")
   @Label("Region Z Position")
   public final int regionPosZ;
   @Name("localPosX")
   @Label("Local X Position")
   public final int localChunkPosX;
   @Name("localPosZ")
   @Label("Local Z Position")
   public final int localChunkPosZ;
   @Name("chunkPosX")
   @Label("Chunk X Position")
   public final int chunkPosX;
   @Name("chunkPosZ")
   @Label("Chunk Z Position")
   public final int chunkPosZ;
   @Name("level")
   @Label("Level Id")
   public final String levelId;
   @Name("dimension")
   @Label("Dimension")
   public final String dimension;
   @Name("type")
   @Label("Type")
   public final String type;
   @Name("compression")
   @Label("Compression")
   public final String compression;
   @Name("bytes")
   @Label("Bytes")
   public final int bytes;

   protected ChunkRegionIoEvent(final RegionStorageInfo info, final ChunkPos chunkPos, final RegionFileVersion version, final int bytes) {
      this.regionPosX = chunkPos.getRegionX();
      this.regionPosZ = chunkPos.getRegionZ();
      this.localChunkPosX = chunkPos.getRegionLocalX();
      this.localChunkPosZ = chunkPos.getRegionLocalZ();
      this.chunkPosX = chunkPos.x();
      this.chunkPosZ = chunkPos.z();
      this.levelId = info.level();
      this.dimension = info.dimension().identifier().toString();
      this.type = info.type();
      this.compression = "standard:" + version.getId();
      this.bytes = bytes;
   }
}
