package net.minecraft.network.syncher;

public class SynchedEntityData$Builder {
   private final SyncedDataHolder entity;
   private final SynchedEntityData$DataItem<?>[] itemsById;

   public SynchedEntityData$Builder(final SyncedDataHolder entity) {
      this.entity = entity;
      this.itemsById = new SynchedEntityData$DataItem[SynchedEntityData.ID_REGISTRY.getCount(entity.getClass())];
   }

   public <T> SynchedEntityData$Builder define(final EntityDataAccessor<T> accessor, final T value) {
      int id = accessor.id();
      if (id >= this.itemsById.length) {
         throw new IllegalArgumentException("Data value id is too big with " + id + "! (Max is " + (this.itemsById.length - 1) + ")");
      } else if (this.itemsById[id] != null) {
         throw new IllegalArgumentException("Duplicate id value for " + id + "!");
      } else if (EntityDataSerializers.getSerializedId(accessor.serializer()) < 0) {
         throw new IllegalArgumentException("Unregistered serializer " + accessor.serializer() + " for " + id + "!");
      } else {
         this.itemsById[accessor.id()] = new SynchedEntityData$DataItem<>(accessor, value);
         return this;
      }
   }

   public SynchedEntityData build() {
      for (int i = 0; i < this.itemsById.length; i++) {
         if (this.itemsById[i] == null) {
            throw new IllegalStateException("Entity " + this.entity.getClass() + " has not defined synched data value " + i);
         }
      }

      return new SynchedEntityData(this.entity, this.itemsById);
   }
}
