package net.minecraft.world.level.storage.loot.entries;

import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

class NestedLootTable$1 extends UniformContainerBase$EntryBase {
   NestedLootTable$1(final NestedLootTable this$0, final Holder val$table) {
      this.val$table = val$tablex;
      Objects.requireNonNull(this$0);
      super(this$0);
   }

   @Override
   public void createItemStack(final Consumer<ItemStack> output, final LootContext context) {
      ((LootTable)this.val$table.value()).getRandomItemsRaw(context, output);
   }
}
