package net.minecraft.network.syncher;

public class SynchedEntityData$DataItem<T> {
   private final EntityDataAccessor<T> accessor;
   private T value;
   private final T initialValue;
   private boolean dirty;

   public SynchedEntityData$DataItem(final EntityDataAccessor<T> accessor, final T initialValue) {
      this.accessor = accessor;
      this.initialValue = initialValue;
      this.value = initialValue;
   }

   public EntityDataAccessor<T> getAccessor() {
      return this.accessor;
   }

   public void setValue(final T value) {
      this.value = value;
   }

   public T getValue() {
      return this.value;
   }

   public boolean isDirty() {
      return this.dirty;
   }

   public void setDirty(final boolean dirty) {
      this.dirty = dirty;
   }

   public boolean isSetToDefault() {
      return this.initialValue.equals(this.value);
   }

   public SynchedEntityData$DataValue<T> value() {
      return SynchedEntityData$DataValue.create(this.accessor, this.value);
   }
}
