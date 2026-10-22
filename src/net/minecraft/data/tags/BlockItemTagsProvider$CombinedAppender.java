package net.minecraft.data.tags;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;
import net.minecraft.references.BlockItemId;
import net.minecraft.tags.BlockItemTagId;

public interface BlockItemTagsProvider$CombinedAppender {
   BlockItemTagsProvider$CombinedAppender addAll(Stream<BlockItemId> ids);

   BlockItemTagsProvider$CombinedAppender addTag(BlockItemTagId id);

   default BlockItemTagsProvider$CombinedAppender addTag(final BlockItemTagId... ids) {
      for (BlockItemTagId id : ids) {
         this.addTag(id);
      }

      return this;
   }

   default BlockItemTagsProvider$CombinedAppender add(final BlockItemId... ids) {
      this.addAll(Arrays.stream(ids));
      return this;
   }

   default BlockItemTagsProvider$CombinedAppender addAll(final Collection<BlockItemId> ids) {
      this.addAll(ids.stream());
      return this;
   }
}
