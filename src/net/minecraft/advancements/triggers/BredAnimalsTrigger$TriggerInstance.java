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
import org.jspecify.annotations.Nullable;

public record BredAnimalsTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player,
   Optional<Holder<LootItemCondition>> parent,
   Optional<Holder<LootItemCondition>> partner,
   Optional<Holder<LootItemCondition>> child
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<BredAnimalsTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(BredAnimalsTrigger$TriggerInstance::player),
            LootItemCondition.CODEC.optionalFieldOf("parent").forGetter(BredAnimalsTrigger$TriggerInstance::parent),
            LootItemCondition.CODEC.optionalFieldOf("partner").forGetter(BredAnimalsTrigger$TriggerInstance::partner),
            LootItemCondition.CODEC.optionalFieldOf("child").forGetter(BredAnimalsTrigger$TriggerInstance::child)
         )
         .apply(i, BredAnimalsTrigger$TriggerInstance::new)
   );

   public static Criterion<BredAnimalsTrigger$TriggerInstance> bredAnimals() {
      return CriteriaTriggers.BRED_ANIMALS
         .createCriterion(new BredAnimalsTrigger$TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
   }

   public static Criterion<BredAnimalsTrigger$TriggerInstance> bredAnimals(final EntityPredicate$Builder child) {
      return CriteriaTriggers.BRED_ANIMALS
         .createCriterion(
            new BredAnimalsTrigger$TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(EntityPredicate.wrap(child)))
         );
   }

   public static Criterion<BredAnimalsTrigger$TriggerInstance> bredAnimals(
      final Optional<EntityPredicate> parent1, final Optional<EntityPredicate> parent2, final Optional<EntityPredicate> child
   ) {
      return CriteriaTriggers.BRED_ANIMALS
         .createCriterion(
            new BredAnimalsTrigger$TriggerInstance(Optional.empty(), EntityPredicate.wrap(parent1), EntityPredicate.wrap(parent2), EntityPredicate.wrap(child))
         );
   }

   public boolean matches(final LootContext parent, final LootContext partner, @Nullable final LootContext child) {
      return !this.child.isPresent() || child != null && this.child.get().value().test(child)
         ? matches(this.parent, parent) && matches(this.partner, partner) || matches(this.parent, partner) && matches(this.partner, parent)
         : false;
   }

   private static boolean matches(final Optional<Holder<LootItemCondition>> predicate, final LootContext context) {
      return predicate.isEmpty() || predicate.get().value().test(context);
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.entityContext(), "parent", this.parent);
      Validatable.validateHolder(validator.entityContext(), "partner", this.partner);
      Validatable.validateHolder(validator.entityContext(), "child", this.child);
   }
}
