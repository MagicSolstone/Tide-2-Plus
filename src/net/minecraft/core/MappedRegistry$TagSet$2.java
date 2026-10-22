package net.minecraft.core;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import net.minecraft.tags.TagKey;

class MappedRegistry$TagSet$2 implements MappedRegistry$TagSet<T> {
   MappedRegistry$TagSet$2(final Map val$tags) {
      this.val$tags = val$tagsx;
   }

   @Override
   public boolean isBound() {
      return true;
   }

   @Override
   public Optional<HolderSet$Named<T>> get(final TagKey<T> id) {
      return Optional.ofNullable((HolderSet$Named)this.val$tags.get(id));
   }

   @Override
   public void forEach(final BiConsumer<? super TagKey<T>, ? super HolderSet$Named<T>> action) {
      this.val$tags.forEach(action);
   }

   @Override
   public Stream<HolderSet$Named<T>> getTags() {
      return this.val$tags.values().stream();
   }
}
