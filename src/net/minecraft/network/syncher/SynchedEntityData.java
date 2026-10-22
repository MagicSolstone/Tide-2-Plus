package net.minecraft.network.syncher;

import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import net.minecraft.util.ClassTreeIdRegistry;
import org.apache.commons.lang3.ObjectUtils;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class SynchedEntityData {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int MAX_ID_VALUE = 254;
   private static final ClassTreeIdRegistry ID_REGISTRY = new ClassTreeIdRegistry();
   private final SyncedDataHolder entity;
   private final SynchedEntityData$DataItem<?>[] itemsById;
   private boolean isDirty;

   private SynchedEntityData(final SyncedDataHolder entity, final SynchedEntityData$DataItem<?>[] itemsById) {
      this.entity = entity;
      this.itemsById = itemsById;
   }

   public static <T> EntityDataAccessor<T> defineId(final Class<? extends SyncedDataHolder> clazz, final EntityDataSerializer<T> type) {
      if (LOGGER.isDebugEnabled()) {
         try {
            Class<?> aClass = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
            if (!aClass.equals(clazz)) {
               LOGGER.debug("defineId called for: {} from {}", new Object[]{clazz, aClass, new RuntimeException()});
            }
         } catch (ClassNotFoundException var3) {
         }
      }

      int id = ID_REGISTRY.define(clazz);
      if (id > 254) {
         throw new IllegalArgumentException("Data value id is too big with " + id + "! (Max is 254)");
      } else {
         return type.createAccessor(id);
      }
   }

   private <T> SynchedEntityData$DataItem<T> getItem(final EntityDataAccessor<T> accessor) {
      return (SynchedEntityData$DataItem<T>)this.itemsById[accessor.id()];
   }

   public <T> T get(final EntityDataAccessor<T> accessor) {
      return this.getItem(accessor).getValue();
   }

   public <T> void set(final EntityDataAccessor<T> accessor, final T value) {
      this.set(accessor, value, false);
   }

   public <T> void set(final EntityDataAccessor<T> accessor, final T value, final boolean forceDirty) {
      SynchedEntityData$DataItem<T> dataItem = this.getItem(accessor);
      if (forceDirty || ObjectUtils.notEqual(value, dataItem.getValue())) {
         dataItem.setValue(value);
         this.entity.onSyncedDataUpdated(accessor);
         dataItem.setDirty(true);
         this.isDirty = true;
      }
   }

   public boolean isDirty() {
      return this.isDirty;
   }

   @Nullable
   public List<SynchedEntityData$DataValue<?>> packDirty() {
      if (!this.isDirty) {
         return null;
      } else {
         this.isDirty = false;
         List<SynchedEntityData$DataValue<?>> result = new ArrayList<>();

         for (SynchedEntityData$DataItem<?> dataItem : this.itemsById) {
            if (dataItem.isDirty()) {
               dataItem.setDirty(false);
               result.add(dataItem.value());
            }
         }

         return result;
      }
   }

   @Nullable
   public List<SynchedEntityData$DataValue<?>> getNonDefaultValues() {
      List<SynchedEntityData$DataValue<?>> result = null;

      for (SynchedEntityData$DataItem<?> dataItem : this.itemsById) {
         if (!dataItem.isSetToDefault()) {
            if (result == null) {
               result = new ArrayList<>();
            }

            result.add(dataItem.value());
         }
      }

      return result;
   }

   public void assignValues(final List<SynchedEntityData$DataValue<?>> items) {
      for (SynchedEntityData$DataValue<?> item : items) {
         SynchedEntityData$DataItem<?> dataItem = this.itemsById[item.id];
         this.assignValue(dataItem, item);
         this.entity.onSyncedDataUpdated(dataItem.getAccessor());
      }

      this.entity.onSyncedDataUpdated(items);
   }

   private <T> void assignValue(final SynchedEntityData$DataItem<T> dataItem, final SynchedEntityData$DataValue<?> item) {
      if (!Objects.equals(item.serializer(), dataItem.accessor.serializer())) {
         throw new IllegalStateException(
            String.format(
               Locale.ROOT,
               "Invalid entity data item type for field %d on entity %s: old=%s(%s), new=%s(%s)",
               dataItem.accessor.id(),
               this.entity,
               dataItem.value,
               dataItem.value.getClass(),
               item.value,
               item.value.getClass()
            )
         );
      } else {
         dataItem.setValue((T)item.value);
      }
   }
}
