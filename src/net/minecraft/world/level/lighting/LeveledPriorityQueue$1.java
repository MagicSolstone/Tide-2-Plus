package net.minecraft.world.level.lighting;

import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
import java.util.Objects;

class LeveledPriorityQueue$1 extends LongLinkedOpenHashSet {
   LeveledPriorityQueue$1(final LeveledPriorityQueue this$0, int expected, float f, final int val$minSize) {
      this.val$minSize = val$minSizex;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(expected, f);
   }

   protected void rehash(final int newN) {
      if (newN > this.val$minSize) {
         super.rehash(newN);
      }
   }
}
