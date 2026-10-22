package net.minecraft.world.level.lighting;

import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;

public class SpatialLongSet extends LongLinkedOpenHashSet {
   private final SpatialLongSet$InternalMap map;

   public SpatialLongSet(final int expected, final float f) {
      super(expected, f);
      this.map = new SpatialLongSet$InternalMap(expected / 64, f);
   }

   public boolean add(final long k) {
      return this.map.addBit(k);
   }

   public boolean rem(final long k) {
      return this.map.removeBit(k);
   }

   public long removeFirstLong() {
      return this.map.removeFirstBit();
   }

   public int size() {
      throw new UnsupportedOperationException();
   }

   public boolean isEmpty() {
      return this.map.isEmpty();
   }
}
