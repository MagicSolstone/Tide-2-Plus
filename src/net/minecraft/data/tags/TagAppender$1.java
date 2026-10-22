package net.minecraft.data.tags;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;

class TagAppender$1 implements TagAppender<T> {
   TagAppender$1(final TagBuilder val$builder) {
      this.val$builder = val$builderx;
   }

   @Override
   public TagAppender<T> add(final ResourceKey<T> element) {
      this.val$builder.addElement(element.identifier());
      return this;
   }

   @Override
   public TagAppender<T> addOptional(final ResourceKey<T> element) {
      this.val$builder.addOptionalElement(element.identifier());
      return this;
   }

   @Override
   public TagAppender<T> addTag(final TagKey<T> tag) {
      this.val$builder.addTag(tag.location());
      return this;
   }

   @Override
   public TagAppender<T> addOptionalTag(final TagKey<T> tag) {
      this.val$builder.addOptionalTag(tag.location());
      return this;
   }
}
