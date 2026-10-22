package net.minecraft.util.datafix.fixes;

import com.mojang.serialization.Dynamic;
import java.util.List;
import org.jspecify.annotations.Nullable;

public final class ChunkProtoTickListFix$PoorMansPalettedContainer {
   private static final long SIZE_BITS = 4L;
   private final List<? extends Dynamic<?>> palette;
   private final long[] data;
   private final int bits;
   private final long mask;
   private final int valuesPerLong;

   public ChunkProtoTickListFix$PoorMansPalettedContainer(final List<? extends Dynamic<?>> palette, final long[] data) {
      this.palette = palette;
      this.data = data;
      this.bits = Math.max(4, ChunkHeightAndBiomeFix.ceillog2(palette.size()));
      this.mask = (1L << this.bits) - 1L;
      this.valuesPerLong = (char)(64 / this.bits);
   }

   @Nullable
   public Dynamic<?> get(final int x, final int y, final int z) {
      int entryCount = this.palette.size();
      if (entryCount < 1) {
         return null;
      } else if (entryCount == 1) {
         return (Dynamic<?>)this.palette.getFirst();
      } else {
         int index = this.getIndex(x, y, z);
         int cellIndex = index / this.valuesPerLong;
         if (cellIndex >= 0 && cellIndex < this.data.length) {
            long cellValue = this.data[cellIndex];
            int bitIndex = (index - cellIndex * this.valuesPerLong) * this.bits;
            int paletteIndex = (int)(cellValue >> bitIndex & this.mask);
            return (Dynamic<?>)(paletteIndex >= 0 && paletteIndex < entryCount ? this.palette.get(paletteIndex) : null);
         } else {
            return null;
         }
      }
   }

   private int getIndex(final int x, final int y, final int z) {
      return (y << 4 | z) << 4 | x;
   }

   public List<? extends Dynamic<?>> palette() {
      return this.palette;
   }

   public long[] data() {
      return this.data;
   }
}
