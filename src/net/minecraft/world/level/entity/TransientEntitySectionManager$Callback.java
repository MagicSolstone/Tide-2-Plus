package net.minecraft.world.level.entity;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity$RemovalReason;

class TransientEntitySectionManager$Callback implements EntityInLevelCallback {
   private final T entity;
   private long currentSectionKey;
   private EntitySection<T> currentSection;

   private TransientEntitySectionManager$Callback(
      final T this$0, final long entity, final EntitySection<T> currentSectionKey, final EntitySection currentSection
   ) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.entity = entity;
      this.currentSectionKey = currentSectionKey;
      this.currentSection = currentSection;
   }

   @Override
   public void onMove() {
      BlockPos pos = this.entity.blockPosition();
      long newSectionPos = SectionPos.asLong(pos);
      if (newSectionPos != this.currentSectionKey) {
         Visibility previousStatus = this.currentSection.getStatus();
         if (!this.currentSection.remove(this.entity)) {
            TransientEntitySectionManager.LOGGER
               .warn("Entity {} wasn't found in section {} (moving to {})", new Object[]{this.entity, SectionPos.of(this.currentSectionKey), newSectionPos});
         }

         this.this$0.removeSectionIfEmpty(this.currentSectionKey, this.currentSection);
         EntitySection<T> newSection = this.this$0.sectionStorage.getOrCreateSection(newSectionPos);
         newSection.add(this.entity);
         this.currentSection = newSection;
         this.currentSectionKey = newSectionPos;
         this.this$0.callbacks.onSectionChange(this.entity);
         if (!this.entity.isAlwaysTicking()) {
            boolean wasTicking = previousStatus.isTicking();
            boolean isTicking = newSection.getStatus().isTicking();
            if (wasTicking && !isTicking) {
               this.this$0.callbacks.onTickingEnd(this.entity);
            } else if (!wasTicking && isTicking) {
               this.this$0.callbacks.onTickingStart(this.entity);
            }
         }
      }
   }

   @Override
   public void onRemove(final Entity$RemovalReason reason) {
      if (!this.currentSection.remove(this.entity)) {
         TransientEntitySectionManager.LOGGER
            .warn("Entity {} wasn't found in section {} (destroying due to {})", new Object[]{this.entity, SectionPos.of(this.currentSectionKey), reason});
      }

      Visibility status = this.currentSection.getStatus();
      if (status.isTicking() || this.entity.isAlwaysTicking()) {
         this.this$0.callbacks.onTickingEnd(this.entity);
      }

      this.this$0.callbacks.onTrackingEnd(this.entity);
      this.this$0.callbacks.onDestroyed(this.entity);
      this.this$0.entityStorage.remove(this.entity);
      this.entity.setLevelCallback(NULL);
      this.this$0.removeSectionIfEmpty(this.currentSectionKey, this.currentSection);
   }
}
