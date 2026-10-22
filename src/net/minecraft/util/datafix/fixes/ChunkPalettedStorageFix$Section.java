package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
import net.minecraft.util.datafix.PackedBitStorage;

class ChunkPalettedStorageFix$Section {
   private final CrudeIncrementalIntIdentityHashBiMap<Dynamic<?>> palette = CrudeIncrementalIntIdentityHashBiMap.create(32);
   private final List<Dynamic<?>> listTag;
   private final Dynamic<?> section;
   private final boolean hasData;
   private final Int2ObjectMap<IntList> toFix = new Int2ObjectLinkedOpenHashMap();
   private final IntList update = new IntArrayList();
   public final int y;
   private final Set<Dynamic<?>> seen = Sets.newIdentityHashSet();
   private final int[] buffer = new int[4096];

   public ChunkPalettedStorageFix$Section(final Dynamic<?> section) {
      this.listTag = Lists.newArrayList();
      this.section = section;
      this.y = section.get("Y").asInt(0);
      this.hasData = section.get("Blocks").result().isPresent();
   }

   public Dynamic<?> getBlock(final int pos) {
      if (pos >= 0 && pos <= 4095) {
         Dynamic<?> tag = this.palette.byId(this.buffer[pos]);
         return tag == null ? ChunkPalettedStorageFix$MappingConstants.AIR : tag;
      } else {
         return ChunkPalettedStorageFix$MappingConstants.AIR;
      }
   }

   public void setBlock(final int idx, final Dynamic<?> blockState) {
      if (this.seen.add(blockState)) {
         this.listTag.add("%%FILTER_ME%%".equals(ChunkPalettedStorageFix.getName(blockState)) ? ChunkPalettedStorageFix$MappingConstants.AIR : blockState);
      }

      this.buffer[idx] = ChunkPalettedStorageFix.idFor(this.palette, blockState);
   }

   public int upgrade(int sides) {
      if (!this.hasData) {
         return sides;
      } else {
         ByteBuffer blocks = (ByteBuffer)this.section.get("Blocks").asByteBufferOpt().result().get();
         ChunkPalettedStorageFix$DataLayer data = this.section
            .get("Data")
            .asByteBufferOpt()
            .map(buffer -> new ChunkPalettedStorageFix$DataLayer(DataFixUtils.toArray(buffer)))
            .result()
            .orElseGet(ChunkPalettedStorageFix$DataLayer::new);
         ChunkPalettedStorageFix$DataLayer addBlocks = this.section
            .get("Add")
            .asByteBufferOpt()
            .map(buffer -> new ChunkPalettedStorageFix$DataLayer(DataFixUtils.toArray(buffer)))
            .result()
            .orElseGet(ChunkPalettedStorageFix$DataLayer::new);
         this.seen.add(ChunkPalettedStorageFix$MappingConstants.AIR);
         ChunkPalettedStorageFix.idFor(this.palette, ChunkPalettedStorageFix$MappingConstants.AIR);
         this.listTag.add(ChunkPalettedStorageFix$MappingConstants.AIR);

         for (int idx = 0; idx < 4096; idx++) {
            int xx = idx & 15;
            int yy = idx >> 8 & 15;
            int zz = idx >> 4 & 15;
            int id = addBlocks.get(xx, yy, zz) << 12 | (blocks.get(idx) & 255) << 4 | data.get(xx, yy, zz);
            if (ChunkPalettedStorageFix$MappingConstants.FIX.get(id >> 4)) {
               this.addFix(id >> 4, idx);
            }

            if (ChunkPalettedStorageFix$MappingConstants.VIRTUAL.get(id >> 4)) {
               int s = ChunkPalettedStorageFix.getSideMask(xx == 0, xx == 15, zz == 0, zz == 15);
               if (s == 0) {
                  this.update.add(idx);
               } else {
                  sides |= s;
               }
            }

            this.setBlock(idx, BlockStateData.getTag(id));
         }

         return sides;
      }
   }

   private void addFix(final int id, final int position) {
      IntList list = (IntList)this.toFix.get(id);
      if (list == null) {
         list = new IntArrayList();
         this.toFix.put(id, list);
      }

      list.add(position);
   }

   public Dynamic<?> write() {
      Dynamic<?> section = this.section;
      if (!this.hasData) {
         return section;
      } else {
         section = section.set("Palette", section.createList(this.listTag.stream()));
         int size = Math.max(4, DataFixUtils.ceillog2(this.seen.size()));
         PackedBitStorage storage = new PackedBitStorage(size, 4096);

         for (int j = 0; j < this.buffer.length; j++) {
            storage.set(j, this.buffer[j]);
         }

         section = section.set("BlockStates", section.createLongList(Arrays.stream(storage.getRaw())));
         section = section.remove("Blocks");
         section = section.remove("Data");
         return section.remove("Add");
      }
   }
}
