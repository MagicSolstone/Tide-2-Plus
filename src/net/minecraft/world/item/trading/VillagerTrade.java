package net.minecraft.world.item.trading;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import org.jspecify.annotations.Nullable;

public class VillagerTrade implements Validatable {
   public static final Codec<VillagerTrade> CODEC = RecordCodecBuilder.create(
         i -> i.group(
               TradeCost.CODEC.fieldOf("wants").forGetter(villagerTrade -> villagerTrade.wants),
               TradeCost.CODEC.optionalFieldOf("additional_wants").forGetter(villagerTrade -> villagerTrade.additionalWants),
               ItemStackTemplate.CODEC.fieldOf("gives").forGetter(villagerTrade -> villagerTrade.gives),
               ContextIntProviders.CODEC.optionalFieldOf("max_uses", ContextIntProviders.exactly(4)).forGetter(villagerTrade -> villagerTrade.maxUses),
               ContextIntProviders.CODEC.optionalFieldOf("xp", ContextIntProviders.exactly(1)).forGetter(villagerTrade -> villagerTrade.xp),
               ContextFloatProviders.CODEC
                  .optionalFieldOf("reputation_discount", ContextFloatProviders.exactly(0.0F))
                  .forGetter(villagerTrade -> villagerTrade.reputationDiscount),
               LootItemCondition.CODEC.optionalFieldOf("merchant_predicate").forGetter(villagerTrade -> villagerTrade.merchantPredicate),
               LootItemFunctions.CODEC.optionalFieldOf("given_item_modifier").forGetter(villagerTrade -> villagerTrade.givenItemModifier),
               RegistryCodecs.holderSet(Registries.ENCHANTMENT)
                  .optionalFieldOf("double_trade_price_enchantments")
                  .forGetter(villagerTrade -> villagerTrade.doubleTradePriceEnchantments)
            )
            .apply(i, VillagerTrade::new)
      )
      .validate(Validatable.validatorForContext(LootContextParamSets.VILLAGER_TRADE));
   private final TradeCost wants;
   private final Optional<TradeCost> additionalWants;
   private final ItemStackTemplate gives;
   private final Optional<Holder<LootItemCondition>> merchantPredicate;
   private final Optional<Holder<LootItemFunction>> givenItemModifier;
   private final Holder<ContextIntProvider> maxUses;
   private final Holder<ContextIntProvider> xp;
   private final Holder<ContextFloatProvider> reputationDiscount;
   private final Optional<HolderSet<Enchantment>> doubleTradePriceEnchantments;

   private VillagerTrade(
      final TradeCost wants,
      final Optional<TradeCost> additionalWants,
      final ItemStackTemplate gives,
      final Holder<ContextIntProvider> maxUses,
      final Holder<ContextIntProvider> xp,
      final Holder<ContextFloatProvider> reputationDiscount,
      final Optional<Holder<LootItemCondition>> merchantPredicate,
      final Optional<Holder<LootItemFunction>> givenItemModifier,
      final Optional<HolderSet<Enchantment>> doubleTradePriceEnchantments
   ) {
      this.wants = wants;
      this.additionalWants = additionalWants;
      this.gives = gives;
      this.maxUses = maxUses;
      this.xp = xp;
      this.reputationDiscount = reputationDiscount;
      this.merchantPredicate = merchantPredicate;
      this.givenItemModifier = givenItemModifier;
      this.doubleTradePriceEnchantments = doubleTradePriceEnchantments;
   }

   @Override
   public void validate(final ValidationContext context) {
      Validatable.validate(context, "wants", this.wants);
      Validatable.validate(context, "additional_wants", this.additionalWants);
      Validatable.validateHolder(context, "max_uses", this.maxUses);
      Validatable.validateHolder(context, "reputation_discount", this.reputationDiscount);
      Validatable.validateHolder(context, "xp", this.xp);
      Validatable.validateHolder(context, "merchant_predicate", this.merchantPredicate);
      Validatable.validateHolder(context, "given_item_modifier", this.givenItemModifier);
   }

   @Nullable
   public MerchantOffer getOffer(final LootContext lootContext) {
      if (this.merchantPredicate.isPresent() && !this.merchantPredicate.get().value().test(lootContext)) {
         return null;
      } else {
         ItemStack result = this.gives.create();
         int additionalCost = 0;
         if (this.givenItemModifier.isPresent()) {
            result = this.givenItemModifier.get().value().apply(result, lootContext);
            if (result.isEmpty()) {
               return null;
            }
         }

         Integer additionalTradeCost = result.remove(DataComponents.ADDITIONAL_TRADE_COST);
         if (additionalTradeCost != null) {
            additionalCost += additionalTradeCost;
         }

         if (this.doubleTradePriceEnchantments.isPresent()) {
            HolderSet<Enchantment> enchantments = this.doubleTradePriceEnchantments.get();
            ItemEnchantments itemEnchantments = result.get(DataComponents.STORED_ENCHANTMENTS);
            if (itemEnchantments != null && itemEnchantments.keySet().stream().anyMatch(enchantments::contains)) {
               additionalCost *= 2;
            }
         }

         ItemCost itemCost = this.wants.toItemCost(lootContext, additionalCost);
         if (itemCost.count() < 1) {
            return null;
         } else {
            Optional<ItemCost> additionalItemCost = this.additionalWants.map(tradeCost -> tradeCost.toItemCost(lootContext, 0));
            return additionalItemCost.isPresent() && additionalItemCost.get().count() < 1
               ? null
               : new MerchantOffer(
                  itemCost,
                  additionalItemCost,
                  result,
                  Math.max(this.maxUses.value().getInt(lootContext), 1),
                  Math.max(this.xp.value().getInt(lootContext), 0),
                  Math.max(this.reputationDiscount.value().getFloat(lootContext), 0.0F)
               );
         }
      }
   }

   public static VillagerTrade$Builder builder(
      final TradeCost wants, final ItemStackTemplate gives, final int maxUses, final int xp, final float reputationDiscount
   ) {
      return new VillagerTrade$Builder(
         wants, gives, ContextIntProviders.exactly(maxUses), ContextIntProviders.exactly(xp), ContextFloatProviders.exactly(reputationDiscount)
      );
   }

   public static VillagerTrade$Builder builder(
      final TradeCost wants, final TradeCost additionalWants, final ItemStackTemplate gives, final int maxUses, final int xp, final float reputationDiscount
   ) {
      return new VillagerTrade$Builder(
            wants, gives, ContextIntProviders.exactly(maxUses), ContextIntProviders.exactly(xp), ContextFloatProviders.exactly(reputationDiscount)
         )
         .additionalWants(additionalWants);
   }
}
