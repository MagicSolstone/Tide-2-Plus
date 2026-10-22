package net.minecraft.world.level.storage.loot.entries;

import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

class LootPoolEntryContainer$1 implements LootPoolEntry {
   LootPoolEntryContainer$1(final LootPoolEntryContainer this$0, final LootPoolEntry val$rawEntry) {
      this.val$rawEntry = val$rawEntryx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public int getWeight(final float luck) {
      return this.val$rawEntry.getWeight(luck);
   }

   @Override
   public void createItemStack(final Consumer<ItemStack> output, final LootContext context) {
      this.val$rawEntry.createItemStack(LootItemFunction.decorate(this.this$0.modifier, output, context), context);
   }
}
