package net.minecraft.world.level.storage.loot.entries;

import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

class SingleEntryContainerBase$1 extends UniformContainerBase$EntryBase {
   SingleEntryContainerBase$1(final SingleEntryContainerBase this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(this$0);
   }

   @Override
   public void createItemStack(final Consumer<ItemStack> output, final LootContext context) {
      this.this$0.createItemStack(output, context);
   }
}
