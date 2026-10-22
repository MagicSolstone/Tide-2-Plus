package net.minecraft.data.tags;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;

public interface TagAppender<T> {
   TagAppender<T> add(ResourceKey<T> element);

   default TagAppender<T> add(final ResourceKey<T>... elements) {
      return this.addAll(Arrays.stream(elements));
   }

   default TagAppender<T> addAll(final Collection<ResourceKey<T>> elements) {
      elements.forEach(this::add);
      return this;
   }

   default TagAppender<T> addAll(final Stream<ResourceKey<T>> elements) {
      elements.forEach(this::add);
      return this;
   }

   TagAppender<T> addOptional(ResourceKey<T> element);

   TagAppender<T> addTag(TagKey<T> tag);

   TagAppender<T> addOptionalTag(TagKey<T> tag);

   static <T> TagAppender<T> forBuilder(final TagBuilder builder) {
      return new TagAppender$1(builder);
   }
}
