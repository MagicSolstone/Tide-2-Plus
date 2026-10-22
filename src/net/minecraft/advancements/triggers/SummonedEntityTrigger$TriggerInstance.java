package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate$Builder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record SummonedEntityTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<Holder<LootItemCondition>> entity)
   implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<SummonedEntityTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(SummonedEntityTrigger$TriggerInstance::player),
            LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(SummonedEntityTrigger$TriggerInstance::entity)
         )
         .apply(i, SummonedEntityTrigger$TriggerInstance::new)
   );

   public static Criterion<SummonedEntityTrigger$TriggerInstance> summonedEntity(final EntityPredicate$Builder predicate) {
      return CriteriaTriggers.SUMMONED_ENTITY
         .createCriterion(new SummonedEntityTrigger$TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(predicate))));
   }

   public boolean matches(final LootContext entity) {
      return this.entity.isEmpty() || this.entity.get().value().test(entity);
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.entityContext(), "entity", this.entity);
   }
}
