package net.minecraft.world.level.storage.loot.functions;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

public class EnchantWithLevelsFunction$Builder extends LootItemConditionalFunction$Builder<EnchantWithLevelsFunction$Builder> {
   private final Holder<ContextIntProvider> levels;
   private Optional<HolderSet<Enchantment>> options = Optional.empty();
   private boolean includeAdditionalCostComponent = false;

   public EnchantWithLevelsFunction$Builder(final Holder<ContextIntProvider> levels) {
      this.levels = levels;
   }

   protected EnchantWithLevelsFunction$Builder getThis() {
      return this;
   }

   public EnchantWithLevelsFunction$Builder withOptions(final HolderSet<Enchantment> tag) {
      this.options = Optional.of(tag);
      return this;
   }

   public EnchantWithLevelsFunction$Builder withOptions(final Optional<HolderSet<Enchantment>> options) {
      this.options = options;
      return this;
   }

   public EnchantWithLevelsFunction$Builder includeAdditionalCostComponent() {
      this.includeAdditionalCostComponent = true;
      return this;
   }

   @Override
   public LootItemFunction build() {
      return new EnchantWithLevelsFunction(this.getCondition(), this.levels, this.options, this.includeAdditionalCostComponent);
   }
}
