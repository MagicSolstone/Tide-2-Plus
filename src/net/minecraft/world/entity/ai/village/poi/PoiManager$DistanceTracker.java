package net.minecraft.world.entity.ai.village.poi;

import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import java.util.Objects;
import net.minecraft.server.level.SectionTracker;

final class PoiManager$DistanceTracker extends SectionTracker {
   private final Long2ByteMap levels;

   PoiManager$DistanceTracker(final PoiManager this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(7, 16, 256);
      this.levels = new Long2ByteOpenHashMap();
      this.levels.defaultReturnValue((byte)7);
   }

   @Override
   protected int getLevelFromSource(final long to) {
      return this.this$0.isVillageCenter(to) ? 0 : 7;
   }

   @Override
   protected int getLevel(final long node) {
      return this.levels.get(node);
   }

   @Override
   protected void setLevel(final long node, final int level) {
      if (level > 6) {
         this.levels.remove(node);
      } else {
         this.levels.put(node, (byte)level);
      }
   }

   public void runAllUpdates() {
      super.runUpdates(Integer.MAX_VALUE);
   }
}
