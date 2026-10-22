package net.minecraft.world.level.storage.loot.entries;

import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

class TagEntry$1 extends UniformContainerBase$EntryBase {
   TagEntry$1(final TagEntry this$0, final Holder val$item) {
      this.val$item = val$itemx;
      Objects.requireNonNull(this$0);
      super(this$0);
   }

   @Override
   public void createItemStack(final Consumer<ItemStack> output, final LootContext context) {
      output.accept(new ItemStack(this.val$item));
   }
}
