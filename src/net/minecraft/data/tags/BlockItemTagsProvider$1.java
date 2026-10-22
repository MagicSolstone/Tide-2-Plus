package net.minecraft.data.tags;

import java.util.stream.Stream;
import net.minecraft.references.BlockItemId;
import net.minecraft.tags.BlockItemTagId;

class BlockItemTagsProvider$1 implements BlockItemTagsProvider$CombinedAppender {
   BlockItemTagsProvider$1(final TagAppender val$appender) {
      this.val$appender = val$appenderx;
   }

   @Override
   public BlockItemTagsProvider$CombinedAppender addAll(final Stream<BlockItemId> ids) {
      this.val$appender.addAll(ids.map(BlockItemId::block));
      return this;
   }

   @Override
   public BlockItemTagsProvider$CombinedAppender addTag(final BlockItemTagId id) {
      this.val$appender.addTag(id.block());
      return this;
   }
}
