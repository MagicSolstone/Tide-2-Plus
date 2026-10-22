package net.minecraft.world.level.storage.loot.entries;

import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

class NestedLootTable$2 extends UniformContainerBase$EntryBase {
   NestedLootTable$2(final NestedLootTable this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(this$0);
   }

   @Override
   public void createItemStack(final Consumer<ItemStack> output, final LootContext context) {
      this.this$0.value.forEach(t -> t.value().getRandomItemsRaw(context, output));
   }
}
