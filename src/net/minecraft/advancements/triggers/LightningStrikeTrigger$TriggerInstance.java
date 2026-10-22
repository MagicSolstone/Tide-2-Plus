package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record LightningStrikeTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player, Optional<Holder<LootItemCondition>> lightning, Optional<Holder<LootItemCondition>> bystander
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<LightningStrikeTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(LightningStrikeTrigger$TriggerInstance::player),
            LootItemCondition.CODEC.optionalFieldOf("lightning").forGetter(LightningStrikeTrigger$TriggerInstance::lightning),
            LootItemCondition.CODEC.optionalFieldOf("bystander").forGetter(LightningStrikeTrigger$TriggerInstance::bystander)
         )
         .apply(i, LightningStrikeTrigger$TriggerInstance::new)
   );

   public static Criterion<LightningStrikeTrigger$TriggerInstance> lightningStrike(
      final Optional<EntityPredicate> lightning, final Optional<EntityPredicate> bystander
   ) {
      return CriteriaTriggers.LIGHTNING_STRIKE
         .createCriterion(new LightningStrikeTrigger$TriggerInstance(Optional.empty(), EntityPredicate.wrap(lightning), EntityPredicate.wrap(bystander)));
   }

   public boolean matches(final LootContext bolt, final List<LootContext> entitiesAround) {
      return this.lightning.isPresent() && !this.lightning.get().value().test(bolt)
         ? false
         : !this.bystander.isPresent() || !entitiesAround.stream().noneMatch(this.bystander.get().value());
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.entityContext(), "lightning", this.lightning);
      Validatable.validateHolder(validator.entityContext(), "bystander", this.bystander);
   }
}
