package net.minecraft.util.debug;

import java.util.Objects;
import org.jspecify.annotations.Nullable;

class TrackingDebugSynchronizer$ValueSource<T> {
   private final DebugValueSource$ValueGetter<T> getter;
   @Nullable
   private T lastSyncedValue;

   private TrackingDebugSynchronizer$ValueSource(final DebugValueSource$ValueGetter<T> getter) {
      this.getter = getter;
   }

   @Nullable
   public DebugSubscription$Update<T> pollUpdate(final DebugSubscription<T> subscription) {
      T newValue = this.getter.get();
      if (!Objects.equals(newValue, this.lastSyncedValue)) {
         this.lastSyncedValue = newValue;
         return subscription.packUpdate(newValue);
      } else {
         return null;
      }
   }
}
