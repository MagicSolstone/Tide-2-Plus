package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;

public class EnchantedCountIncreaseFunction$Builder extends LootItemConditionalFunction$Builder<EnchantedCountIncreaseFunction$Builder> {
   private final Holder<Enchantment> enchantment;
   private final Holder<ContextFloatProvider> count;
   private int limit = 0;

   public EnchantedCountIncreaseFunction$Builder(final Holder<Enchantment> enchantment, final Holder<ContextFloatProvider> count) {
      this.enchantment = enchantment;
      this.count = count;
   }

   protected EnchantedCountIncreaseFunction$Builder getThis() {
      return this;
   }

   public EnchantedCountIncreaseFunction$Builder setLimit(final int limit) {
      this.limit = limit;
      return this;
   }

   @Override
   public LootItemFunction build() {
      return new EnchantedCountIncreaseFunction(this.getCondition(), this.enchantment, this.count, this.limit);
   }
}
