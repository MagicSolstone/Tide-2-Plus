package net.minecraft.data.tags;

import java.util.Objects;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

class VanillaBlockTagsProvider$1 extends BlockItemTagAppender<Block> {
   VanillaBlockTagsProvider$1(final VanillaBlockTagsProvider this$0, final TagAppender original) {
      Objects.requireNonNull(this$0);
      super(original);
   }

   @Override
   protected ResourceKey<Block> convertElement(final BlockItemId element) {
      return element.block();
   }
}
