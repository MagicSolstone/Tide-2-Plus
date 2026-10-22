package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public class EnchantWithLevelsFunction extends LootItemConditionalFunction {
   public static final MapCodec<EnchantWithLevelsFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> commonFields(i)
         .and(
            i.group(
               ContextIntProviders.CODEC.fieldOf("levels").forGetter(f -> f.levels),
               RegistryCodecs.holderSet(Registries.ENCHANTMENT).optionalFieldOf("options").forGetter(f -> f.options),
               Codec.BOOL.optionalFieldOf("include_additional_cost_component", false).forGetter(f -> f.includeAdditionalCostComponent)
            )
         )
         .apply(i, EnchantWithLevelsFunction::new)
   );
   private final Holder<ContextIntProvider> levels;
   private final Optional<HolderSet<Enchantment>> options;
   private final boolean includeAdditionalCostComponent;

   private EnchantWithLevelsFunction(
      final Optional<Holder<LootItemCondition>> condition,
      final Holder<ContextIntProvider> levels,
      final Optional<HolderSet<Enchantment>> options,
      final boolean includeAdditionalCostComponent
   ) {
      super(condition);
      this.levels = levels;
      this.options = options;
      this.includeAdditionalCostComponent = includeAdditionalCostComponent;
   }

   @Override
   public MapCodec<EnchantWithLevelsFunction> codec() {
      return MAP_CODEC;
   }

   @Override
   public Set<ContextKey<?>> getReferencedContextParams() {
      return this.includeAdditionalCostComponent ? Set.of(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED) : Set.of();
   }

   @Override
   public void validate(final ValidationContext context) {
      super.validate(context);
      Validatable.validateHolder(context, "levels", this.levels);
   }

   @Override
   public ItemStack run(final ItemStack itemStack, final LootContext context) {
      RandomSource random = context.getRandom();
      RegistryAccess registryAccess = context.getLevel().registryAccess();
      int enchantmentCost = this.levels.value().getInt(context);
      ItemStack result = EnchantmentHelper.enchantItem(random, itemStack, enchantmentCost, registryAccess, this.options);
      if (this.includeAdditionalCostComponent
         && context.hasParameter(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED)
         && !result.isEmpty()
         && enchantmentCost > 0) {
         result.set(DataComponents.ADDITIONAL_TRADE_COST, enchantmentCost);
      }

      return result;
   }

   public static EnchantWithLevelsFunction$Builder enchantWithLevels(final HolderGetter<Enchantment> enchantments, final Holder<ContextIntProvider> levels) {
      return new EnchantWithLevelsFunction$Builder(levels).withOptions(enchantments.getOrThrow(EnchantmentTags.ON_RANDOM_LOOT));
   }
}
