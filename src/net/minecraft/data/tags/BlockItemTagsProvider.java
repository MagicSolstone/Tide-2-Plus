package net.minecraft.data.tags;

import java.util.function.Function;
import net.minecraft.tags.BlockItemTagId;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public abstract class BlockItemTagsProvider {
   private final Function<BlockItemTagId, BlockItemTagsProvider$CombinedAppender> tagSupplier;

   protected BlockItemTagsProvider(final Function<BlockItemTagId, BlockItemTagsProvider$CombinedAppender> tagSupplier) {
      this.tagSupplier = tagSupplier;
   }

   protected BlockItemTagsProvider$CombinedAppender tag(final BlockItemTagId tag) {
      return this.tagSupplier.apply(tag);
   }

   protected abstract void run();

   public static BlockItemTagsProvider$CombinedAppender wrapForBlocks(final TagAppender<Block> appender) {
      return new BlockItemTagsProvider$1(appender);
   }

   public static BlockItemTagsProvider$CombinedAppender wrapForItems(final TagAppender<Item> appender) {
      return new BlockItemTagsProvider$2(appender);
   }
}
