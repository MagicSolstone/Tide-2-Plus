package net.minecraft.util.parsing.packrat;

import org.jspecify.annotations.Nullable;

record CachedParseState$CacheEntry<T>(@Nullable T value, int markAfterParse) {
   public static final CachedParseState$CacheEntry<?> NEGATIVE = new CachedParseState$CacheEntry(null, -1);

   public static <T> CachedParseState$CacheEntry<T> negativeEntry() {
      return (CachedParseState$CacheEntry<T>)NEGATIVE;
   }
}
