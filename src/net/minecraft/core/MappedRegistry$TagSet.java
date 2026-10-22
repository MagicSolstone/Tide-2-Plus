package net.minecraft.core;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import net.minecraft.tags.TagKey;

interface MappedRegistry$TagSet<T> {
   static <T> MappedRegistry$TagSet<T> unbound() {
      return new MappedRegistry$TagSet$1();
   }

   static <T> MappedRegistry$TagSet<T> fromMap(final Map<TagKey<T>, HolderSet$Named<T>> tags) {
      return new MappedRegistry$TagSet$2(tags);
   }

   boolean isBound();

   Optional<HolderSet$Named<T>> get(TagKey<T> id);

   void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet$Named<T>> action);

   Stream<HolderSet$Named<T>> getTags();
}
