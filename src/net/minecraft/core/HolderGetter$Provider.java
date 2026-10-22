package net.minecraft.core;

import java.util.Optional;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public interface HolderGetter$Provider {
   <T> Optional<? extends HolderGetter<T>> lookup(final ResourceKey<? extends Registry<? extends T>> key);

   default <T> HolderGetter<T> lookupOrThrow(final ResourceKey<? extends Registry<? extends T>> key) {
      return (HolderGetter<T>)this.lookup(key).orElseThrow(() -> new IllegalStateException("Registry " + key.identifier() + " not found"));
   }

   default <T> Optional<Holder$Reference<T>> get(final ResourceKey<T> id) {
      return this.lookup(id.registryKey()).flatMap(l -> l.get(id));
   }

   default <T> Holder$Reference<T> getOrThrow(final ResourceKey<T> id) {
      return this.lookup(id.registryKey()).flatMap(l -> l.get(id)).orElseThrow(() -> new IllegalStateException("Missing element " + id));
   }

   default <T> Optional<HolderSet$Named<T>> get(final TagKey<T> id) {
      return this.lookup(id.registry()).flatMap(l -> l.get(id));
   }

   default <T> HolderSet$Named<T> getOrThrow(final TagKey<T> id) {
      return this.lookup(id.registry()).flatMap(l -> l.get(id)).orElseThrow(() -> new IllegalStateException("Missing tag " + id));
   }
}
