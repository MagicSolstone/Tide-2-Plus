package net.minecraft.client.multiplayer;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class ClientChunkCache extends ChunkSource {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final LevelChunk emptyChunk;
   private final LevelLightEngine lightEngine;
   private volatile ClientChunkCache$Storage storage;
   private final ClientLevel level;

   public ClientChunkCache(final ClientLevel level, final int serverChunkRadius) {
      this.level = level;
      this.emptyChunk = new EmptyLevelChunk(level, new ChunkPos(0, 0), level.registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS));
      this.lightEngine = new LevelLightEngine(this, true, level.dimensionType().hasSkyLight());
      this.storage = new ClientChunkCache$Storage(this, calculateStorageRange(serverChunkRadius));
   }

   @Override
   public LevelLightEngine getLightEngine() {
      return this.lightEngine;
   }

   private static boolean isValidChunk(@Nullable final LevelChunk chunk, final int x, final int z) {
      if (chunk == null) {
         return false;
      } else {
         ChunkPos pos = chunk.getPos();
         return pos.x() == x && pos.z() == z;
      }
   }

   public void drop(final ChunkPos pos) {
      if (this.storage.inRange(pos.x(), pos.z())) {
         int index = this.storage.getIndex(pos.x(), pos.z());
         LevelChunk currentChunk = this.storage.getChunk(index);
         if (isValidChunk(currentChunk, pos.x(), pos.z())) {
            this.storage.drop(index, currentChunk);
         }
      }
   }

   @Nullable
   public LevelChunk getChunk(final int x, final int z, final ChunkStatus targetStatus, final boolean loadOrGenerate) {
      if (this.storage.inRange(x, z)) {
         LevelChunk chunk = this.storage.getChunk(this.storage.getIndex(x, z));
         if (isValidChunk(chunk, x, z)) {
            return chunk;
         }
      }

      return loadOrGenerate ? this.emptyChunk : null;
   }

   @Override
   public BlockGetter getLevel() {
      return this.level;
   }

   public void replaceBiomes(final int chunkX, final int chunkZ, final FriendlyByteBuf readBuffer) {
      if (!this.storage.inRange(chunkX, chunkZ)) {
         LOGGER.warn("Ignoring chunk since it's not in the view range: {}, {}", chunkX, chunkZ);
      } else {
         int index = this.storage.getIndex(chunkX, chunkZ);
         LevelChunk chunk = this.storage.chunks.get(index);
         if (!isValidChunk(chunk, chunkX, chunkZ)) {
            LOGGER.warn("Ignoring chunk since it's not present: {}, {}", chunkX, chunkZ);
         } else {
            chunk.replaceBiomes(readBuffer);
         }
      }
   }

   @Nullable
   public LevelChunk replaceWithPacketData(final int chunkX, final int chunkZ, final ClientboundLevelChunkPacketData chunkData) {
      if (!this.storage.inRange(chunkX, chunkZ)) {
         LOGGER.warn("Ignoring chunk since it's not in the view range: {}, {}", chunkX, chunkZ);
         return null;
      } else {
         int index = this.storage.getIndex(chunkX, chunkZ);
         LevelChunk chunk = this.storage.chunks.get(index);
         ChunkPos pos = new ChunkPos(chunkX, chunkZ);
         if (!isValidChunk(chunk, chunkX, chunkZ)) {
            chunk = new LevelChunk(this.level, pos);
            chunk.replaceWithPacketData(chunkX, chunkZ, chunkData);
            this.storage.replace(index, chunk);
         } else {
            chunk.replaceWithPacketData(chunkX, chunkZ, chunkData);
            this.storage.refreshEmptySections(chunk);
         }

         this.level.onChunkLoaded(pos);
         return chunk;
      }
   }

   @Override
   public void tick(final BooleanSupplier haveTime, final boolean tickChunks) {
   }

   public void updateViewCenter(final int x, final int z) {
      this.storage.viewCenterX = x;
      this.storage.viewCenterZ = z;
   }

   public void updateViewRadius(final int viewRange) {
      int chunkRadius = this.storage.chunkRadius;
      int newChunkRadius = calculateStorageRange(viewRange);
      if (chunkRadius != newChunkRadius) {
         ClientChunkCache$Storage newStorage = new ClientChunkCache$Storage(this, newChunkRadius);
         newStorage.viewCenterX = this.storage.viewCenterX;
         newStorage.viewCenterZ = this.storage.viewCenterZ;

         for (int i = 0; i < this.storage.chunks.length(); i++) {
            LevelChunk chunk = this.storage.chunks.get(i);
            if (chunk != null) {
               ChunkPos pos = chunk.getPos();
               if (newStorage.inRange(pos.x(), pos.z())) {
                  newStorage.replace(newStorage.getIndex(pos.x(), pos.z()), chunk);
               }
            }
         }

         this.storage = newStorage;
      }
   }

   private static int calculateStorageRange(final int viewRange) {
      return Math.max(2, viewRange) + 3;
   }

   @Override
   public String gatherStats() {
      return this.storage.chunks.length() + ", " + this.getLoadedChunksCount();
   }

   @Override
   public int getLoadedChunksCount() {
      return this.storage.chunkCount;
   }

   @Override
   public void onLightUpdate(final LightLayer layer, final SectionPos pos) {
      Minecraft.getInstance().levelExtractor.setSectionDirty(pos.x(), pos.y(), pos.z());
   }

   public LongOpenHashSet addedEmptySections() {
      return this.storage.addedEmptySections[this.storage.updatingSetsIndex];
   }

   public LongOpenHashSet removedEmptySections() {
      return this.storage.removedEmptySections[this.storage.updatingSetsIndex];
   }

   public LongOpenHashSet addedLoadedChunks() {
      return this.storage.addedLoadedChunks[this.storage.updatingSetsIndex];
   }

   public LongOpenHashSet removedLoadedChunks() {
      return this.storage.removedLoadedChunks[this.storage.updatingSetsIndex];
   }

   public void flipUpdateTrackingSets() {
      this.storage.updatingSetsIndex = (this.storage.updatingSetsIndex + 1) % 2;
      this.storage.addedEmptySections[this.storage.updatingSetsIndex].clear();
      this.storage.removedEmptySections[this.storage.updatingSetsIndex].clear();
      this.storage.addedLoadedChunks[this.storage.updatingSetsIndex].clear();
      this.storage.removedLoadedChunks[this.storage.updatingSetsIndex].clear();
   }

   @Override
   public void onSectionEmptinessChanged(final int sectionX, final int sectionY, final int sectionZ, final boolean empty) {
      this.storage.onSectionEmptinessChanged(sectionX, sectionY, sectionZ, empty);
   }
}
