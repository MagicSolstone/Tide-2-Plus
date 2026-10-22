package net.minecraft.nbt;

interface NbtOps$ListCollector {
   NbtOps$ListCollector accept(Tag t);

   default NbtOps$ListCollector acceptAll(final Iterable<Tag> tags) {
      NbtOps$ListCollector collector = this;

      for (Tag tag : tags) {
         collector = collector.accept(tag);
      }

      return collector;
   }

   Tag result();
}
