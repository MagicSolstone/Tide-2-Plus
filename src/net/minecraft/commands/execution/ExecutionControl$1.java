package net.minecraft.commands.execution;

import org.jspecify.annotations.Nullable;

class ExecutionControl$1 implements ExecutionControl<T> {
   ExecutionControl$1(final ExecutionContext val$context, final Frame val$frame) {
      this.val$context = val$contextx;
      this.val$frame = val$framex;
   }

   @Override
   public void queueNext(final EntryAction<T> action) {
      this.val$context.queueNext(new CommandQueueEntry(this.val$frame, action));
   }

   @Override
   public void tracer(@Nullable final TraceCallbacks tracer) {
      this.val$context.tracer(tracer);
   }

   @Nullable
   @Override
   public TraceCallbacks tracer() {
      return this.val$context.tracer();
   }

   @Override
   public Frame currentFrame() {
      return this.val$frame;
   }
}
