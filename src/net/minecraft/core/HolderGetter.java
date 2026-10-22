package net.minecraft.core;

import java.util.Optional;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;

public interface HolderGetter<T> extends HolderOwner<T> {
   Optional<Holder$Reference<T>> get(final ResourceKey<T> id);

   default Holder$Reference<T> getOrThrow(final ResourceKey<T> id) {
      return this.get(id).orElseThrow(() -> new IllegalStateException("Missing element " + id));
   }

   Optional<HolderSet$Named<T>> get(final TagKey<T> id);

   default HolderSet$Named<T> getOrThrow(final TagKey<T> id) {
      return this.get(id).orElseThrow(() -> new IllegalStateException("Missing tag " + id));
   }

   default Optional<Holder<T>> getRandomElementOf(final TagKey<T> tag, final RandomSource random) {
      return this.get(tag).flatMap(holderSet -> holderSet.getRandomElement(random));
   }
}
