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

public record TameAnimalTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<Holder<LootItemCondition>> entity)
   implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<TameAnimalTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TameAnimalTrigger$TriggerInstance::player),
            LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(TameAnimalTrigger$TriggerInstance::entity)
         )
         .apply(i, TameAnimalTrigger$TriggerInstance::new)
   );

   public static Criterion<TameAnimalTrigger$TriggerInstance> tamedAnimal() {
      return CriteriaTriggers.TAME_ANIMAL.createCriterion(new TameAnimalTrigger$TriggerInstance(Optional.empty(), Optional.empty()));
   }

   public static Criterion<TameAnimalTrigger$TriggerInstance> tamedAnimal(final EntityPredicate$Builder entity) {
      return CriteriaTriggers.TAME_ANIMAL.createCriterion(new TameAnimalTrigger$TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity))));
   }

   public boolean matches(final LootContext animal) {
      return this.entity.isEmpty() || this.entity.get().value().test(animal);
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.entityContext(), "entity", this.entity);
   }
}
