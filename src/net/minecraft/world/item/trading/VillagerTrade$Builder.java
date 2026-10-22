package net.minecraft.world.item.trading;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction$Builder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

public class VillagerTrade$Builder {
   private final TradeCost wants;
   private final ItemStackTemplate gives;
   private final Holder<ContextIntProvider> maxUses;
   private final Holder<ContextIntProvider> xp;
   private final Holder<ContextFloatProvider> reputationDiscount;
   private Optional<TradeCost> additionalWants = Optional.empty();
   private Optional<Holder<LootItemCondition>> merchantPredicate = Optional.empty();
   private Optional<HolderSet<Enchantment>> doubleTradePriceEnchantments = Optional.empty();
   private final Builder<Holder<LootItemFunction>> givenItemModifiers = ImmutableList.builder();

   public VillagerTrade$Builder(
      final TradeCost wants,
      final ItemStackTemplate gives,
      final Holder<ContextIntProvider> maxUses,
      final Holder<ContextIntProvider> xp,
      final Holder<ContextFloatProvider> reputationDiscount
   ) {
      this.wants = wants;
      this.gives = gives;
      this.maxUses = maxUses;
      this.xp = xp;
      this.reputationDiscount = reputationDiscount;
   }

   public VillagerTrade$Builder additionalWants(final TradeCost additionalWants) {
      this.additionalWants = Optional.of(additionalWants);
      return this;
   }

   public VillagerTrade$Builder merchantPredicate(final Holder<LootItemCondition> merchantPredicate) {
      this.merchantPredicate = Optional.of(merchantPredicate);
      return this;
   }

   public VillagerTrade$Builder addModifier(final LootItemFunction$Builder function) {
      this.givenItemModifiers.add(Holder.direct(function.build()));
      return this;
   }

   @SafeVarargs
   public final VillagerTrade$Builder addModifiers(final Holder<LootItemFunction>... functions) {
      return this.addModifiers(Arrays.asList(functions));
   }

   public VillagerTrade$Builder addModifiers(final Collection<Holder<LootItemFunction>> function) {
      this.givenItemModifiers.addAll(function);
      return this;
   }

   public VillagerTrade$Builder doubleTradePriceEnchantments(final HolderSet<Enchantment> doubleTradePriceEnchantments) {
      this.doubleTradePriceEnchantments = Optional.of(doubleTradePriceEnchantments);
      return this;
   }

   public VillagerTrade build() {
      return new VillagerTrade(
         this.wants,
         this.additionalWants,
         this.gives,
         this.maxUses,
         this.xp,
         this.reputationDiscount,
         this.merchantPredicate,
         FunctionUserBuilder.buildFunction(this.givenItemModifiers.build()),
         this.doubleTradePriceEnchantments
      );
   }
}
