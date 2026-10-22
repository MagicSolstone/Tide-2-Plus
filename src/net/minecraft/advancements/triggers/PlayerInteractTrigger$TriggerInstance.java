package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.ItemPredicate$Builder;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record PlayerInteractTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player, Optional<ItemPredicate> item, Optional<Holder<LootItemCondition>> entity
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<PlayerInteractTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(PlayerInteractTrigger$TriggerInstance::player),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(PlayerInteractTrigger$TriggerInstance::item),
            LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(PlayerInteractTrigger$TriggerInstance::entity)
         )
         .apply(i, PlayerInteractTrigger$TriggerInstance::new)
   );

   public static Criterion<PlayerInteractTrigger$TriggerInstance> itemUsedOnEntity(
      final Optional<Holder<LootItemCondition>> player, final ItemPredicate$Builder item, final Optional<Holder<LootItemCondition>> entity
   ) {
      return CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY
         .createCriterion(new PlayerInteractTrigger$TriggerInstance(player, Optional.of(item.build()), entity));
   }

   public static Criterion<PlayerInteractTrigger$TriggerInstance> equipmentSheared(
      final Optional<Holder<LootItemCondition>> player, final ItemPredicate$Builder item, final Optional<Holder<LootItemCondition>> entity
   ) {
      return CriteriaTriggers.PLAYER_SHEARED_EQUIPMENT.createCriterion(new PlayerInteractTrigger$TriggerInstance(player, Optional.of(item.build()), entity));
   }

   public static Criterion<PlayerInteractTrigger$TriggerInstance> equipmentSheared(
      final ItemPredicate$Builder item, final Optional<Holder<LootItemCondition>> entity
   ) {
      return CriteriaTriggers.PLAYER_SHEARED_EQUIPMENT
         .createCriterion(new PlayerInteractTrigger$TriggerInstance(Optional.empty(), Optional.of(item.build()), entity));
   }

   public static Criterion<PlayerInteractTrigger$TriggerInstance> itemUsedOnEntity(
      final ItemPredicate$Builder item, final Optional<Holder<LootItemCondition>> entity
   ) {
      return itemUsedOnEntity(Optional.empty(), item, entity);
   }

   public boolean matches(final ItemStack itemStack, final LootContext interactedWith) {
      return this.item.isPresent() && !this.item.get().test((ItemInstance)itemStack)
         ? false
         : this.entity.isEmpty() || this.entity.get().value().test(interactedWith);
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.entityContext(), "entity", this.entity);
   }
}
