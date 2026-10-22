package net.minecraft.data.tags;

import java.util.Optional;
import java.util.function.Function;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;

@FunctionalInterface
public interface TagsProvider$TagLookup<T> extends Function<TagKey<T>, Optional<TagBuilder>> {
   static <T> TagsProvider$TagLookup<T> empty() {
      return id -> Optional.empty();
   }

   default boolean contains(final TagKey<T> key) {
      return this.apply(key).isPresent();
   }
}
