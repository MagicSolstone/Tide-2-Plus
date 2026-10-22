package net.minecraft.world.level.biome;

import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import java.util.Objects;

class Biome$1 extends Long2FloatLinkedOpenHashMap {
   Biome$1(final Biome this$0, int expected, float f) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(expected, f);
   }

   protected void rehash(final int newN) {
   }
}
