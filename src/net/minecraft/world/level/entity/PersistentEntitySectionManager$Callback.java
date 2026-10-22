package net.minecraft.world.level.entity;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity$RemovalReason;

class PersistentEntitySectionManager$Callback implements EntityInLevelCallback {
   private final T entity;
   private long currentSectionKey;
   private EntitySection<T> currentSection;

   private PersistentEntitySectionManager$Callback(
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
            PersistentEntitySectionManager.LOGGER
               .warn("Entity {} wasn't found in section {} (moving to {})", new Object[]{this.entity, SectionPos.of(this.currentSectionKey), newSectionPos});
         }

         this.this$0.removeSectionIfEmpty(this.currentSectionKey, this.currentSection);
         EntitySection<T> newSection = this.this$0.sectionStorage.getOrCreateSection(newSectionPos);
         newSection.add(this.entity);
         this.currentSection = newSection;
         this.currentSectionKey = newSectionPos;
         this.updateStatus(previousStatus, newSection.getStatus());
      }
   }

   private void updateStatus(final Visibility previousStatus, final Visibility newStatus) {
      Visibility effectivePreviousStatus = PersistentEntitySectionManager.getEffectiveStatus(this.entity, previousStatus);
      Visibility effectiveNewStatus = PersistentEntitySectionManager.getEffectiveStatus(this.entity, newStatus);
      if (effectivePreviousStatus == effectiveNewStatus) {
         if (effectiveNewStatus.isAccessible()) {
            this.this$0.callbacks.onSectionChange(this.entity);
         }
      } else {
         boolean wasAccessible = effectivePreviousStatus.isAccessible();
         boolean isAccessible = effectiveNewStatus.isAccessible();
         if (wasAccessible && !isAccessible) {
            this.this$0.stopTracking(this.entity);
         } else if (!wasAccessible && isAccessible) {
            this.this$0.startTracking(this.entity);
         }

         boolean wasTicking = effectivePreviousStatus.isTicking();
         boolean isTicking = effectiveNewStatus.isTicking();
         if (wasTicking && !isTicking) {
            this.this$0.stopTicking(this.entity);
         } else if (!wasTicking && isTicking) {
            this.this$0.startTicking(this.entity);
         }

         if (isAccessible) {
            this.this$0.callbacks.onSectionChange(this.entity);
         }
      }
   }

   @Override
   public void onRemove(final Entity$RemovalReason reason) {
      if (!this.currentSection.remove(this.entity)) {
         PersistentEntitySectionManager.LOGGER
            .warn("Entity {} wasn't found in section {} (destroying due to {})", new Object[]{this.entity, SectionPos.of(this.currentSectionKey), reason});
      }

      Visibility status = PersistentEntitySectionManager.getEffectiveStatus(this.entity, this.currentSection.getStatus());
      if (status.isTicking()) {
         this.this$0.stopTicking(this.entity);
      }

      if (status.isAccessible()) {
         this.this$0.stopTracking(this.entity);
      }

      if (reason.shouldDestroy()) {
         this.this$0.callbacks.onDestroyed(this.entity);
      }

      this.this$0.knownUuids.remove(this.entity.getUUID());
      this.entity.setLevelCallback(NULL);
      this.this$0.removeSectionIfEmpty(this.currentSectionKey, this.currentSection);
   }
}
