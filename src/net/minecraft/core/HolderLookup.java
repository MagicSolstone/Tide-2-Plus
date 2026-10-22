package net.minecraft.core;

import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public interface HolderLookup<T> extends HolderGetter<T> {
   Stream<Holder$Reference<T>> listElements();

   default Stream<ResourceKey<T>> listElementIds() {
      return this.listElements().map(Holder$Reference::key);
   }

   Stream<HolderSet$Named<T>> listTags();

   default Stream<TagKey<T>> listTagIds() {
      return this.listTags().map(HolderSet$Named::key);
   }
}
