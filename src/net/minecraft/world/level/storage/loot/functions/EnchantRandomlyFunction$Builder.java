package net.minecraft.world.level.storage.loot.functions;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantRandomlyFunction$Builder extends LootItemConditionalFunction$Builder<EnchantRandomlyFunction$Builder> {
   private Optional<HolderSet<Enchantment>> options = Optional.empty();
   private boolean onlyCompatible = true;
   private boolean includeAdditionalCostComponent = false;

   protected EnchantRandomlyFunction$Builder getThis() {
      return this;
   }

   public EnchantRandomlyFunction$Builder withEnchantment(final Holder<Enchantment> enchantment) {
      this.options = Optional.of(HolderSet.direct(enchantment));
      return this;
   }

   public EnchantRandomlyFunction$Builder withOneOf(final HolderSet<Enchantment> enchantments) {
      this.options = Optional.of(enchantments);
      return this;
   }

   public EnchantRandomlyFunction$Builder withOptions(final HolderSet<Enchantment> enchantments) {
      this.options = Optional.of(enchantments);
      return this;
   }

   public EnchantRandomlyFunction$Builder allowingIncompatibleEnchantments() {
      this.onlyCompatible = false;
      return this;
   }

   public EnchantRandomlyFunction$Builder includeAdditionalCostComponent() {
      this.includeAdditionalCostComponent = true;
      return this;
   }

   @Override
   public LootItemFunction build() {
      return new EnchantRandomlyFunction(this.getCondition(), this.options, this.onlyCompatible, this.includeAdditionalCostComponent);
   }
}
