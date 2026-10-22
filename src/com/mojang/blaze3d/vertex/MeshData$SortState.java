package com.mojang.blaze3d.vertex;

import com.mojang.renderpearl.api.pipeline.IndexType;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.lang3.mutable.MutableLong;
import org.lwjgl.system.MemoryUtil;

@Environment(EnvType.CLIENT)
public record MeshData$SortState(CompactVectorArray centroids, IndexType indexType) {
   public ByteBufferBuilder$Result buildSortedIndexBuffer(final ByteBufferBuilder target, final VertexSorting sorting) {
      int[] startIndices = sorting.sort(this.centroids);
      long pointer = target.reserve(startIndices.length * 6 * this.indexType.bytes);
      writeIndices(startIndices, this.indexWriter(pointer, this.indexType));
      return target.build();
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public void writeSortedIndexBuffer(final ByteBuffer target, final VertexSorting sorting) {
      IntConsumer output = switch (this.indexType) {
         case SHORT -> value -> target.putShort((short)value);
         case INT -> target::putInt;
      };
      writeIndices(sorting.sort(this.centroids), output);
   }

   private static void writeIndices(final int[] startIndices, final IntConsumer output) {
      for (int startIndex : startIndices) {
         output.accept(startIndex * 4 + 0);
         output.accept(startIndex * 4 + 1);
         output.accept(startIndex * 4 + 2);
         output.accept(startIndex * 4 + 2);
         output.accept(startIndex * 4 + 3);
         output.accept(startIndex * 4 + 0);
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private IntConsumer indexWriter(final long pointer, final IndexType indexType) {
      MutableLong nextIndex = new MutableLong(pointer);

      return switch (indexType) {
         case SHORT -> value -> MemoryUtil.memPutShort(nextIndex.getAndAdd(2L), (short)value);
         case INT -> value -> MemoryUtil.memPutInt(nextIndex.getAndAdd(4L), value);
      };
   }
}
