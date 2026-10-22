package net.minecraft.nbt;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public sealed interface CollectionTag extends Tag, Iterable<Tag> permits ListTag, ByteArrayTag, IntArrayTag, LongArrayTag {
   void clear();

   boolean setTag(int index, Tag tag);

   boolean addTag(int index, Tag tag);

   Tag remove(int index);

   Tag get(int index);

   int size();

   default boolean isEmpty() {
      return this.size() == 0;
   }

   @Override
   default Iterator<Tag> iterator() {
      return new CollectionTag$1(this);
   }

   default Stream<Tag> stream() {
      return StreamSupport.stream(this.spliterator(), false);
   }
}
