package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate$Builder;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record TradeTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player, Optional<Holder<LootItemCondition>> villager, Optional<ItemPredicate> item
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<TradeTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TradeTrigger$TriggerInstance::player),
            LootItemCondition.CODEC.optionalFieldOf("villager").forGetter(TradeTrigger$TriggerInstance::villager),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TradeTrigger$TriggerInstance::item)
         )
         .apply(i, TradeTrigger$TriggerInstance::new)
   );

   public static Criterion<TradeTrigger$TriggerInstance> tradedWithVillager() {
      return CriteriaTriggers.TRADE.createCriterion(new TradeTrigger$TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
   }

   public static Criterion<TradeTrigger$TriggerInstance> tradedWithVillager(final EntityPredicate$Builder player) {
      return CriteriaTriggers.TRADE
         .createCriterion(new TradeTrigger$TriggerInstance(Optional.of(EntityPredicate.wrap(player)), Optional.empty(), Optional.empty()));
   }

   public boolean matches(final LootContext villager, final ItemStack itemStack) {
      return this.villager.isPresent() && !this.villager.get().value().test(villager)
         ? false
         : !this.item.isPresent() || this.item.get().test((ItemInstance)itemStack);
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.entityContext(), "villager", this.villager);
   }
}
