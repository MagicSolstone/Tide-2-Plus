package net.minecraft.client.multiplayer;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceArray;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
final class ClientChunkCache$Storage {
   private static final int UPDATE_TRACKING_BUFFERS = 2;
   private final AtomicReferenceArray<LevelChunk> chunks;
   private final LongOpenHashSet[] addedEmptySections;
   private final LongOpenHashSet[] removedEmptySections;
   private final LongOpenHashSet[] addedLoadedChunks;
   private final LongOpenHashSet[] removedLoadedChunks;
   private int updatingSetsIndex;
   private final int chunkRadius;
   private final int viewRange;
   private volatile int viewCenterX;
   private volatile int viewCenterZ;
   private int chunkCount;

   private ClientChunkCache$Storage(final ClientChunkCache this$0, final int chunkRadius) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.addedEmptySections = new LongOpenHashSet[2];
      this.removedEmptySections = new LongOpenHashSet[2];
      this.addedLoadedChunks = new LongOpenHashSet[2];
      this.removedLoadedChunks = new LongOpenHashSet[2];
      this.chunkRadius = chunkRadius;
      this.viewRange = chunkRadius * 2 + 1;
      this.chunks = new AtomicReferenceArray<>(this.viewRange * this.viewRange);

      for (int i = 0; i < 2; i++) {
         this.addedEmptySections[i] = new LongOpenHashSet();
         this.removedEmptySections[i] = new LongOpenHashSet();
         this.addedLoadedChunks[i] = new LongOpenHashSet();
         this.removedLoadedChunks[i] = new LongOpenHashSet();
      }
   }

   private int getIndex(final int chunkX, final int chunkZ) {
      return Math.floorMod(chunkZ, this.viewRange) * this.viewRange + Math.floorMod(chunkX, this.viewRange);
   }

   private void replace(final int index, @Nullable final LevelChunk newChunk) {
      LevelChunk removedChunk = this.chunks.getAndSet(index, newChunk);
      if (removedChunk != null) {
         this.chunkCount--;
         this.onChunkRemoved(removedChunk);
         this.this$0.level.unload(removedChunk);
      }

      if (newChunk != null) {
         this.chunkCount++;
         this.onChunkAdded(newChunk);
      }
   }

   private void drop(final int index, final LevelChunk oldChunk) {
      if (this.chunks.compareAndSet(index, oldChunk, null)) {
         this.chunkCount--;
         this.onChunkRemoved(oldChunk);
      }

      this.this$0.level.unload(oldChunk);
   }

   public void onSectionEmptinessChanged(final int sectionX, final int sectionY, final int sectionZ, final boolean empty) {
      if (this.inRange(sectionX, sectionZ)) {
         long sectionNode = SectionPos.asLong(sectionX, sectionY, sectionZ);
         if (empty) {
            this.markSectionEmpty(sectionNode);
         } else {
            this.markSectionNotEmpty(sectionNode);
         }
      }
   }

   private void onChunkRemoved(final LevelChunk chunk) {
      ChunkPos chunkPos = chunk.getPos();
      long chunkNode = chunkPos.pack();
      this.addedLoadedChunks[this.updatingSetsIndex].remove(chunkNode);
      this.removedLoadedChunks[this.updatingSetsIndex].add(chunkNode);
      LevelChunkSection[] sections = chunk.getSections();

      for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
         this.markSectionEmpty(SectionPos.asLong(chunkPos.x(), chunk.getSectionYFromSectionIndex(sectionIndex), chunkPos.z()));
      }
   }

   private void onChunkAdded(final LevelChunk chunk) {
      ChunkPos chunkPos = chunk.getPos();
      long chunkNode = chunkPos.pack();
      this.removedLoadedChunks[this.updatingSetsIndex].remove(chunkNode);
      this.addedLoadedChunks[this.updatingSetsIndex].add(chunkNode);
      LevelChunkSection[] sections = chunk.getSections();

      for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
         LevelChunkSection section = sections[sectionIndex];
         long sectionNode = SectionPos.asLong(chunkPos.x(), chunk.getSectionYFromSectionIndex(sectionIndex), chunkPos.z());
         if (section.hasOnlyAir()) {
            this.markSectionEmpty(sectionNode);
         } else {
            this.markSectionNotEmpty(sectionNode);
         }
      }
   }

   private void refreshEmptySections(final LevelChunk chunk) {
      ChunkPos chunkPos = chunk.getPos();
      LevelChunkSection[] sections = chunk.getSections();

      for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
         LevelChunkSection section = sections[sectionIndex];
         long sectionNode = SectionPos.asLong(chunkPos.x(), chunk.getSectionYFromSectionIndex(sectionIndex), chunkPos.z());
         if (section.hasOnlyAir()) {
            this.markSectionEmpty(sectionNode);
         } else {
            this.markSectionNotEmpty(sectionNode);
         }
      }
   }

   private void markSectionEmpty(final long sectionNode) {
      this.removedEmptySections[this.updatingSetsIndex].remove(sectionNode);
      this.addedEmptySections[this.updatingSetsIndex].add(sectionNode);
   }

   private void markSectionNotEmpty(final long sectionNode) {
      this.addedEmptySections[this.updatingSetsIndex].remove(sectionNode);
      this.removedEmptySections[this.updatingSetsIndex].add(sectionNode);
   }

   private boolean inRange(final int chunkX, final int chunkZ) {
      return Math.abs(chunkX - this.viewCenterX) <= this.chunkRadius && Math.abs(chunkZ - this.viewCenterZ) <= this.chunkRadius;
   }

   @Nullable
   public LevelChunk getChunk(final int index) {
      return this.chunks.get(index);
   }

   private void dumpChunks(final String file) {
      try (FileOutputStream stream = new FileOutputStream(file)) {
         int chunkRadius = this.this$0.storage.chunkRadius;

         for (int z = this.viewCenterZ - chunkRadius; z <= this.viewCenterZ + chunkRadius; z++) {
            for (int x = this.viewCenterX - chunkRadius; x <= this.viewCenterX + chunkRadius; x++) {
               LevelChunk chunk = this.this$0.storage.chunks.get(this.this$0.storage.getIndex(x, z));
               if (chunk != null) {
                  ChunkPos pos = chunk.getPos();
                  stream.write((pos.x() + "\t" + pos.z() + "\t" + chunk.isEmpty() + "\n").getBytes(StandardCharsets.UTF_8));
               }
            }
         }
      } catch (IOException var10) {
         ClientChunkCache.LOGGER.error("Failed to dump chunks to file {}", file, var10);
      }
   }
}
