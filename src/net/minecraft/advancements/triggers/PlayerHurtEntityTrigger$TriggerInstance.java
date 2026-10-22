package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.DamagePredicate;
import net.minecraft.advancements.predicates.DamagePredicate$Builder;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record PlayerHurtEntityTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player, Optional<DamagePredicate> damage, Optional<Holder<LootItemCondition>> entity
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<PlayerHurtEntityTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(PlayerHurtEntityTrigger$TriggerInstance::player),
            DamagePredicate.CODEC.optionalFieldOf("damage").forGetter(PlayerHurtEntityTrigger$TriggerInstance::damage),
            LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(PlayerHurtEntityTrigger$TriggerInstance::entity)
         )
         .apply(i, PlayerHurtEntityTrigger$TriggerInstance::new)
   );

   public static Criterion<PlayerHurtEntityTrigger$TriggerInstance> playerHurtEntity() {
      return CriteriaTriggers.PLAYER_HURT_ENTITY
         .createCriterion(new PlayerHurtEntityTrigger$TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
   }

   public static Criterion<PlayerHurtEntityTrigger$TriggerInstance> playerHurtEntityWithDamage(final Optional<DamagePredicate> damage) {
      return CriteriaTriggers.PLAYER_HURT_ENTITY.createCriterion(new PlayerHurtEntityTrigger$TriggerInstance(Optional.empty(), damage, Optional.empty()));
   }

   public static Criterion<PlayerHurtEntityTrigger$TriggerInstance> playerHurtEntityWithDamage(final DamagePredicate$Builder damage) {
      return CriteriaTriggers.PLAYER_HURT_ENTITY
         .createCriterion(new PlayerHurtEntityTrigger$TriggerInstance(Optional.empty(), Optional.of(damage.build()), Optional.empty()));
   }

   public static Criterion<PlayerHurtEntityTrigger$TriggerInstance> playerHurtEntity(final Optional<EntityPredicate> entity) {
      return CriteriaTriggers.PLAYER_HURT_ENTITY
         .createCriterion(new PlayerHurtEntityTrigger$TriggerInstance(Optional.empty(), Optional.empty(), EntityPredicate.wrap(entity)));
   }

   public static Criterion<PlayerHurtEntityTrigger$TriggerInstance> playerHurtEntity(
      final Optional<DamagePredicate> damage, final Optional<EntityPredicate> entity
   ) {
      return CriteriaTriggers.PLAYER_HURT_ENTITY
         .createCriterion(new PlayerHurtEntityTrigger$TriggerInstance(Optional.empty(), damage, EntityPredicate.wrap(entity)));
   }

   public static Criterion<PlayerHurtEntityTrigger$TriggerInstance> playerHurtEntity(
      final DamagePredicate$Builder damage, final Optional<EntityPredicate> entity
   ) {
      return CriteriaTriggers.PLAYER_HURT_ENTITY
         .createCriterion(new PlayerHurtEntityTrigger$TriggerInstance(Optional.empty(), Optional.of(damage.build()), EntityPredicate.wrap(entity)));
   }

   public boolean matches(
      final ServerPlayer player,
      final LootContext victim,
      final DamageSource source,
      final float originalDamage,
      final float actualDamage,
      final boolean blocked
   ) {
      return this.damage.isPresent() && !this.damage.get().matches(player, source, originalDamage, actualDamage, blocked)
         ? false
         : !this.entity.isPresent() || this.entity.get().value().test(victim);
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.entityContext(), "entity", this.entity);
   }
}
