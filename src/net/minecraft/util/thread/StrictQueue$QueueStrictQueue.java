package net.minecraft.util.thread;

import java.util.Queue;
import org.jspecify.annotations.Nullable;

public final class StrictQueue$QueueStrictQueue implements StrictQueue<Runnable> {
   private final Queue<Runnable> queue;

   public StrictQueue$QueueStrictQueue(final Queue<Runnable> queue) {
      this.queue = queue;
   }

   @Nullable
   @Override
   public Runnable pop() {
      return this.queue.poll();
   }

   @Override
   public boolean push(final Runnable t) {
      return this.queue.add(t);
   }

   @Override
   public boolean isEmpty() {
      return this.queue.isEmpty();
   }

   @Override
   public int size() {
      return this.queue.size();
   }
}
