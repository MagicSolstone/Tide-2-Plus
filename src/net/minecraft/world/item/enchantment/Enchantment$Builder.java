package net.minecraft.world.item.enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentMap$Builder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition$Builder;

public class Enchantment$Builder {
   private final Enchantment$EnchantmentDefinition definition;
   private HolderSet<Enchantment> exclusiveSet = HolderSet.empty();
   private final Map<DataComponentType<?>, List<?>> effectLists = new HashMap<>();
   private final DataComponentMap$Builder effectMapBuilder = DataComponentMap.builder();

   public Enchantment$Builder(final Enchantment$EnchantmentDefinition definition) {
      this.definition = definition;
   }

   public Enchantment$Builder exclusiveWith(final HolderSet<Enchantment> set) {
      this.exclusiveSet = set;
      return this;
   }

   public <E> Enchantment$Builder withEffect(
      final DataComponentType<List<ConditionalEffect<E>>> type, final E effect, final LootItemCondition$Builder condition
   ) {
      this.getEffectsList(type).add(new ConditionalEffect<>(effect, Optional.of(Holder.direct(condition.build()))));
      return this;
   }

   public <E> Enchantment$Builder withEffect(final DataComponentType<List<ConditionalEffect<E>>> type, final E effect) {
      this.getEffectsList(type).add(new ConditionalEffect<>(effect, Optional.empty()));
      return this;
   }

   public <E> Enchantment$Builder withEffect(
      final DataComponentType<List<TargetedConditionalEffect<E>>> type,
      final EnchantmentTarget enchanted,
      final EnchantmentTarget affected,
      final E effect,
      final LootItemCondition$Builder condition
   ) {
      this.getEffectsList(type).add(new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.of(Holder.direct(condition.build()))));
      return this;
   }

   public <E> Enchantment$Builder withEffect(
      final DataComponentType<List<TargetedConditionalEffect<E>>> type, final EnchantmentTarget enchanted, final EnchantmentTarget affected, final E effect
   ) {
      this.getEffectsList(type).add(new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.empty()));
      return this;
   }

   public Enchantment$Builder withEffect(final DataComponentType<List<EnchantmentAttributeEffect>> type, final EnchantmentAttributeEffect effect) {
      this.getEffectsList(type).add(effect);
      return this;
   }

   public <E> Enchantment$Builder withSpecialEffect(final DataComponentType<E> type, final E effect) {
      this.effectMapBuilder.set(type, effect);
      return this;
   }

   public Enchantment$Builder withEffect(final DataComponentType<Unit> type) {
      this.effectMapBuilder.set(type, Unit.INSTANCE);
      return this;
   }

   private <E> List<E> getEffectsList(final DataComponentType<List<E>> type) {
      return (List<E>)this.effectLists.computeIfAbsent(type, k -> {
         ArrayList<E> newList = new ArrayList<>();
         this.effectMapBuilder.set(type, newList);
         return newList;
      });
   }

   public Enchantment build(final Identifier descriptionKey) {
      return new Enchantment(
         Component.translatable(Util.makeDescriptionId("enchantment", descriptionKey)), this.definition, this.exclusiveSet, this.effectMapBuilder.build()
      );
   }
}
