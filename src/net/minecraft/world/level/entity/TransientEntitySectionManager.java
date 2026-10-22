package net.minecraft.world.level.entity;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.SectionPos;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.level.ChunkPos;
import org.slf4j.Logger;

public class TransientEntitySectionManager<T extends EntityAccess> {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final LevelCallback<T> callbacks;
   private final EntityLookup<T> entityStorage;
   private final EntitySectionStorage<T> sectionStorage;
   private final LongSet tickingChunks = new LongOpenHashSet();
   private final LevelEntityGetter<T> entityGetter;

   public TransientEntitySectionManager(final Class<T> entityClass, final LevelCallback<T> callbacks) {
      this.entityStorage = new EntityLookup<>();
      this.sectionStorage = new EntitySectionStorage<>(entityClass, key -> this.tickingChunks.contains(key) ? Visibility.TICKING : Visibility.TRACKED);
      this.callbacks = callbacks;
      this.entityGetter = new LevelEntityGetterAdapter<>(this.entityStorage, this.sectionStorage);
   }

   public void startTicking(final ChunkPos pos) {
      long chunkKey = pos.pack();
      this.tickingChunks.add(chunkKey);
      this.sectionStorage.getExistingSectionsInChunk(chunkKey).forEach(section -> {
         Visibility previousStatus = section.updateChunkStatus(Visibility.TICKING);
         if (!previousStatus.isTicking()) {
            section.getEntities().filter(e -> !e.isAlwaysTicking()).forEach(this.callbacks::onTickingStart);
         }
      });
   }

   public void stopTicking(final ChunkPos pos) {
      long chunkKey = pos.pack();
      this.tickingChunks.remove(chunkKey);
      this.sectionStorage.getExistingSectionsInChunk(chunkKey).forEach(section -> {
         Visibility previousStatus = section.updateChunkStatus(Visibility.TRACKED);
         if (previousStatus.isTicking()) {
            section.getEntities().filter(e -> !e.isAlwaysTicking()).forEach(this.callbacks::onTickingEnd);
         }
      });
   }

   public LevelEntityGetter<T> getEntityGetter() {
      return this.entityGetter;
   }

   public void addEntity(final T entity) {
      this.entityStorage.add(entity);
      long sectionKey = SectionPos.asLong(entity.blockPosition());
      EntitySection<T> entitySection = this.sectionStorage.getOrCreateSection(sectionKey);
      entitySection.add(entity);
      entity.setLevelCallback(new TransientEntitySectionManager$Callback((T)this, (long)entity, sectionKey, entitySection));
      this.callbacks.onCreated(entity);
      this.callbacks.onTrackingStart(entity);
      if (entity.isAlwaysTicking() || entitySection.getStatus().isTicking()) {
         this.callbacks.onTickingStart(entity);
      }
   }

   @VisibleForDebug
   public int count() {
      return this.entityStorage.count();
   }

   private void removeSectionIfEmpty(final long sectionPos, final EntitySection<T> section) {
      if (section.isEmpty()) {
         this.sectionStorage.remove(sectionPos);
      }
   }

   @VisibleForDebug
   public String gatherStats() {
      return this.entityStorage.count() + "," + this.sectionStorage.count() + "," + this.tickingChunks.size();
   }
}
