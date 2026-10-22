package net.minecraft.nbt;

import it.unimi.dsi.fastutil.ints.IntArrayList;

class NbtOps$IntListCollector implements NbtOps$ListCollector {
   private final IntArrayList values = new IntArrayList();

   public NbtOps$IntListCollector(final int[] initialValues) {
      this.values.addElements(0, initialValues);
   }

   @Override
   public NbtOps$ListCollector accept(final Tag tag) {
      if (tag instanceof IntTag intTag) {
         this.values.add(intTag.intValue());
         return this;
      } else {
         return new NbtOps$GenericListCollector(this.values).accept(tag);
      }
   }

   @Override
   public Tag result() {
      return new IntArrayTag(this.values.toIntArray());
   }
}
