package net.minecraft.nbt;

import it.unimi.dsi.fastutil.longs.LongArrayList;

class NbtOps$LongListCollector implements NbtOps$ListCollector {
   private final LongArrayList values = new LongArrayList();

   public NbtOps$LongListCollector(final long[] initialValues) {
      this.values.addElements(0, initialValues);
   }

   @Override
   public NbtOps$ListCollector accept(final Tag tag) {
      if (tag instanceof LongTag longTag) {
         this.values.add(longTag.longValue());
         return this;
      } else {
         return new NbtOps$GenericListCollector(this.values).accept(tag);
      }
   }

   @Override
   public Tag result() {
      return new LongArrayTag(this.values.toLongArray());
   }
}
