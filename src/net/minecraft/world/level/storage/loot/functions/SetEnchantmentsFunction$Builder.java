package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

public class SetEnchantmentsFunction$Builder extends LootItemConditionalFunction$Builder<SetEnchantmentsFunction$Builder> {
   private final Builder<Holder<Enchantment>, Holder<ContextIntProvider>> enchantments = ImmutableMap.builder();
   private final boolean add;

   public SetEnchantmentsFunction$Builder() {
      this(false);
   }

   public SetEnchantmentsFunction$Builder(final boolean add) {
      this.add = add;
   }

   protected SetEnchantmentsFunction$Builder getThis() {
      return this;
   }

   public SetEnchantmentsFunction$Builder withEnchantment(final Holder<Enchantment> enchantment, final Holder<ContextIntProvider> levelProvider) {
      this.enchantments.put(enchantment, levelProvider);
      return this;
   }

   @Override
   public LootItemFunction build() {
      return new SetEnchantmentsFunction(this.getCondition(), this.enchantments.build(), this.add);
   }
}
