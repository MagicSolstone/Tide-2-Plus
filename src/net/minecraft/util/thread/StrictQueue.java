package net.minecraft.util.thread;

import org.jspecify.annotations.Nullable;

public interface StrictQueue<T extends Runnable> {
   @Nullable
   Runnable pop();

   boolean push(final T t);

   boolean isEmpty();

   int size();
}
