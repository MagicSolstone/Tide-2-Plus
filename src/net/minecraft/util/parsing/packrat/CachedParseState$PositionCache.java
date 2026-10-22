package net.minecraft.util.parsing.packrat;

import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

class CachedParseState$PositionCache {
   public static final int ENTRY_STRIDE = 2;
   private static final int NOT_FOUND = -1;
   private Object[] atomCache = new Object[16];
   private int nextKey;

   private CachedParseState$PositionCache() {
   }

   public int findKeyIndex(final Atom<?> key) {
      for (int i = 0; i < this.nextKey; i += 2) {
         if (this.atomCache[i] == key) {
            return i;
         }
      }

      return -1;
   }

   public int allocateNewEntry(final Atom<?> key) {
      int newKeyIndex = this.nextKey;
      this.nextKey += 2;
      int newValueIndex = newKeyIndex + 1;
      int currentSize = this.atomCache.length;
      if (newValueIndex >= currentSize) {
         int newSize = Util.growByHalf(currentSize, newValueIndex + 1);
         Object[] newCache = new Object[newSize];
         System.arraycopy(this.atomCache, 0, newCache, 0, currentSize);
         this.atomCache = newCache;
      }

      this.atomCache[newKeyIndex] = key;
      return newKeyIndex;
   }

   @Nullable
   public <T> CachedParseState$CacheEntry<T> getValue(final int keyIndex) {
      return (CachedParseState$CacheEntry<T>)this.atomCache[keyIndex + 1];
   }

   public void setValue(final int keyIndex, final CachedParseState$CacheEntry<?> entry) {
      this.atomCache[keyIndex + 1] = entry;
   }
}
