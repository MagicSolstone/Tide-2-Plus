package net.minecraft.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

class Util$10 implements Function<T, R> {
   private final Map<T, R> cache;

   Util$10(final Function val$function) {
      this.val$function = val$functionx;
      this.cache = new ConcurrentHashMap();
   }

   @Override
   public R apply(final T arg) {
      return (R)this.cache.computeIfAbsent(arg, this.val$function);
   }

   @Override
   public String toString() {
      return "memoize/1[function=" + this.val$function + ", size=" + this.cache.size() + "]";
   }
}
