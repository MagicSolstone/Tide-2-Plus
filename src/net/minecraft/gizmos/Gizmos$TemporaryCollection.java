package net.minecraft.gizmos;

import org.jspecify.annotations.Nullable;

public class Gizmos$TemporaryCollection implements AutoCloseable {
   @Nullable
   private final GizmoCollector old = Gizmos.collector.get();
   private boolean closed;

   private Gizmos$TemporaryCollection() {
   }

   @Override
   public void close() {
      if (!this.closed) {
         this.closed = true;
         Gizmos.collector.set(this.old);
      }
   }
}
