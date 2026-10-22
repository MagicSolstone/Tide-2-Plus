package net.minecraft.nbt;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;

class NbtOps$ByteListCollector implements NbtOps$ListCollector {
   private final ByteArrayList values = new ByteArrayList();

   public NbtOps$ByteListCollector(final byte[] initialValues) {
      this.values.addElements(0, initialValues);
   }

   @Override
   public NbtOps$ListCollector accept(final Tag tag) {
      if (tag instanceof ByteTag byteTag) {
         this.values.add(byteTag.byteValue());
         return this;
      } else {
         return new NbtOps$GenericListCollector(this.values).accept(tag);
      }
   }

   @Override
   public Tag result() {
      return new ByteArrayTag(this.values.toByteArray());
   }
}
