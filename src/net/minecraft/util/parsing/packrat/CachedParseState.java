package net.minecraft.util.parsing.packrat;

import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

public abstract class CachedParseState<S> implements ParseState<S> {
   private CachedParseState$PositionCache[] positionCache = new CachedParseState$PositionCache[256];
   private final ErrorCollector<S> errorCollector;
   private final Scope scope = new Scope();
   private CachedParseState$SimpleControl[] controlCache = new CachedParseState$SimpleControl[16];
   private int nextControlToReturn;
   private final CachedParseState<S>.Silent silent = new CachedParseState$Silent(this);

   protected CachedParseState(final ErrorCollector<S> errorCollector) {
      this.errorCollector = errorCollector;
   }

   @Override
   public Scope scope() {
      return this.scope;
   }

   @Override
   public ErrorCollector<S> errorCollector() {
      return this.errorCollector;
   }

   @Nullable
   @Override
   public <T> T parse(final NamedRule<S, T> rule) {
      int markBeforeParse = this.mark();
      CachedParseState$PositionCache positionCache = this.getCacheForPosition(markBeforeParse);
      int entryIndex = positionCache.findKeyIndex(rule.name());
      if (entryIndex != -1) {
         CachedParseState$CacheEntry<T> value = positionCache.getValue(entryIndex);
         if (value != null) {
            if (value == CachedParseState$CacheEntry.NEGATIVE) {
               return null;
            }

            this.restore(value.markAfterParse);
            return value.value;
         }
      } else {
         entryIndex = positionCache.allocateNewEntry(rule.name());
      }

      T result = rule.value().parse(this);
      CachedParseState$CacheEntry<T> entry;
      if (result == null) {
         entry = CachedParseState$CacheEntry.negativeEntry();
      } else {
         int markAfterParse = this.mark();
         entry = new CachedParseState$CacheEntry<>(result, markAfterParse);
      }

      positionCache.setValue(entryIndex, entry);
      return result;
   }

   private CachedParseState$PositionCache getCacheForPosition(final int index) {
      int currentSize = this.positionCache.length;
      if (index >= currentSize) {
         int newSize = Util.growByHalf(currentSize, index + 1);
         CachedParseState$PositionCache[] newCache = new CachedParseState$PositionCache[newSize];
         System.arraycopy(this.positionCache, 0, newCache, 0, currentSize);
         this.positionCache = newCache;
      }

      CachedParseState$PositionCache result = this.positionCache[index];
      if (result == null) {
         result = new CachedParseState$PositionCache();
         this.positionCache[index] = result;
      }

      return result;
   }

   @Override
   public Control acquireControl() {
      int currentSize = this.controlCache.length;
      if (this.nextControlToReturn >= currentSize) {
         int newSize = Util.growByHalf(currentSize, this.nextControlToReturn + 1);
         CachedParseState$SimpleControl[] newControlCache = new CachedParseState$SimpleControl[newSize];
         System.arraycopy(this.controlCache, 0, newControlCache, 0, currentSize);
         this.controlCache = newControlCache;
      }

      int controlIndex = this.nextControlToReturn++;
      CachedParseState$SimpleControl entry = this.controlCache[controlIndex];
      if (entry == null) {
         entry = new CachedParseState$SimpleControl();
         this.controlCache[controlIndex] = entry;
      } else {
         entry.reset();
      }

      return entry;
   }

   @Override
   public void releaseControl() {
      this.nextControlToReturn--;
   }

   @Override
   public ParseState<S> silent() {
      return this.silent;
   }
}
