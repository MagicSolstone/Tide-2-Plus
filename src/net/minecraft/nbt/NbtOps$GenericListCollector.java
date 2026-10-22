package net.minecraft.nbt;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;

class NbtOps$GenericListCollector implements NbtOps$ListCollector {
   private final ListTag result = new ListTag();

   private NbtOps$GenericListCollector() {
   }

   private NbtOps$GenericListCollector(final ListTag initial) {
      this.result.addAll(initial);
   }

   public NbtOps$GenericListCollector(final IntArrayList initials) {
      initials.forEach(v -> this.result.add(IntTag.valueOf(v)));
   }

   public NbtOps$GenericListCollector(final ByteArrayList initials) {
      initials.forEach(v -> this.result.add(ByteTag.valueOf(v)));
   }

   public NbtOps$GenericListCollector(final LongArrayList initials) {
      initials.forEach(v -> this.result.add(LongTag.valueOf(v)));
   }

   @Override
   public NbtOps$ListCollector accept(final Tag tag) {
      this.result.add(tag);
      return this;
   }

   @Override
   public Tag result() {
      return this.result;
   }
}
