package net.minecraft.client.renderer.block;

import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class BlockModelLighter$Cache$1 extends Long2IntLinkedOpenHashMap {
   BlockModelLighter$Cache$1(final BlockModelLighter$Cache this$0, int expected, float f) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(expected, f);
   }

   protected void rehash(final int newN) {
   }
}
