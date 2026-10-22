package net.minecraft.core;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import net.minecraft.tags.TagKey;

class MappedRegistry$TagSet$1 implements MappedRegistry$TagSet<T> {
   @Override
   public boolean isBound() {
      return false;
   }

   @Override
   public Optional<HolderSet$Named<T>> get(final TagKey<T> id) {
      throw new IllegalStateException("Tags not bound, trying to access " + id);
   }

   @Override
   public void forEach(final BiConsumer<? super TagKey<T>, ? super HolderSet$Named<T>> action) {
      throw new IllegalStateException("Tags not bound");
   }

   @Override
   public Stream<HolderSet$Named<T>> getTags() {
      throw new IllegalStateException("Tags not bound");
   }
}
