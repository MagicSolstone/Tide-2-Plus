package net.minecraft.util;

import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

class Util$11 implements BiFunction<T, U, R> {
   private final Map<Pair<T, U>, R> cache;

   Util$11(final BiFunction val$function) {
      this.val$function = val$functionx;
      this.cache = new ConcurrentHashMap<>();
   }

   @Override
   public R apply(final T a, final U b) {
      return (R)this.cache.computeIfAbsent(Pair.of(a, b), args -> function.apply(args.getFirst(), args.getSecond()));
   }

   @Override
   public String toString() {
      return "memoize/2[function=" + this.val$function + ", size=" + this.cache.size() + "]";
   }
}
