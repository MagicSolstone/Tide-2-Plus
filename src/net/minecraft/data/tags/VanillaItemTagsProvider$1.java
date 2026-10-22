package net.minecraft.data.tags;

import java.util.Objects;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

class VanillaItemTagsProvider$1 extends BlockItemTagAppender<Item> {
   VanillaItemTagsProvider$1(final VanillaItemTagsProvider this$0, final TagAppender original) {
      Objects.requireNonNull(this$0);
      super(original);
   }

   @Override
   protected ResourceKey<Item> convertElement(final BlockItemId element) {
      return element.item();
   }
}
