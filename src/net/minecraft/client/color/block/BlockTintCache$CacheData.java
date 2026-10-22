package net.minecraft.client.color.block;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
class BlockTintCache$CacheData {
   private final Int2ObjectArrayMap<int[]> cache = new Int2ObjectArrayMap(16);
   private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
   private static final int BLOCKS_PER_LAYER = Mth.square(16);
   private volatile boolean invalidated;

   private BlockTintCache$CacheData() {
   }

   public int[] getLayer(final int y) {
      this.lock.readLock().lock();

      try {
         int[] existing = (int[])this.cache.get(y);
         if (existing != null) {
            return existing;
         }
      } finally {
         this.lock.readLock().unlock();
      }

      this.lock.writeLock().lock();

      int[] var12;
      try {
         var12 = (int[])this.cache.computeIfAbsent(y, n -> this.allocateLayer());
      } finally {
         this.lock.writeLock().unlock();
      }

      return var12;
   }

   private int[] allocateLayer() {
      int[] newCache = new int[BLOCKS_PER_LAYER];
      Arrays.fill(newCache, -1);
      return newCache;
   }

   public boolean isInvalidated() {
      return this.invalidated;
   }

   public void invalidate() {
      this.invalidated = true;
   }
}
