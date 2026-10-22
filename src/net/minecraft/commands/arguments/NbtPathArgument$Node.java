package net.minecraft.commands.arguments;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.nbt.Tag;

interface NbtPathArgument$Node {
   void getTag(Tag parent, final List<Tag> output);

   void getOrCreateTag(Tag parent, Supplier<Tag> child, final List<Tag> output);

   Tag createPreferredParentTag();

   int setTag(Tag parent, Supplier<Tag> toAdd);

   int removeTag(Tag parent);

   default List<Tag> get(final List<Tag> tags) {
      return this.collect(tags, this::getTag);
   }

   default List<Tag> getOrCreate(final List<Tag> tags, final Supplier<Tag> child) {
      return this.collect(tags, (tag, output) -> this.getOrCreateTag(tag, child, output));
   }

   default List<Tag> collect(final List<Tag> tags, final BiConsumer<Tag, List<Tag>> collector) {
      List<Tag> result = Lists.newArrayList();

      for (Tag tag : tags) {
         collector.accept(tag, result);
      }

      return result;
   }
}
