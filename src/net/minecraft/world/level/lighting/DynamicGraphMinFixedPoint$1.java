package net.minecraft.world.level.lighting;

import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import java.util.Objects;

class DynamicGraphMinFixedPoint$1 extends Long2ByteOpenHashMap {
   DynamicGraphMinFixedPoint$1(final DynamicGraphMinFixedPoint this$0, int expected, float f, final int val$minMapSize) {
      this.val$minMapSize = val$minMapSizex;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(expected, f);
   }

   protected void rehash(final int newN) {
      if (newN > this.val$minMapSize) {
         super.rehash(newN);
      }
   }
}
