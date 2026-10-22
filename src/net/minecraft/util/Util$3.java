package net.minecraft.util;

import java.util.function.Supplier;

class Util$3 implements Supplier<T> {
   Util$3(final Supplier val$task, final String val$name) {
      this.val$task = val$taskx;
      this.val$name = val$namex;
   }

   @Override
   public T get() {
      return (T)this.val$task.get();
   }

   @Override
   public String toString() {
      return this.val$name;
   }
}
