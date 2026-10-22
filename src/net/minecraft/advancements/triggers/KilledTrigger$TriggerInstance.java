package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.DamageSourcePredicate;
import net.minecraft.advancements.predicates.DamageSourcePredicate$Builder;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate$Builder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record KilledTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player, Optional<Holder<LootItemCondition>> entity, Optional<DamageSourcePredicate> killingBlow
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<KilledTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(KilledTrigger$TriggerInstance::player),
            LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(KilledTrigger$TriggerInstance::entity),
            DamageSourcePredicate.CODEC.optionalFieldOf("killing_blow").forGetter(KilledTrigger$TriggerInstance::killingBlow)
         )
         .apply(i, KilledTrigger$TriggerInstance::new)
   );

   public static Criterion<KilledTrigger$TriggerInstance> playerKilledEntity(final Optional<EntityPredicate> entity) {
      return CriteriaTriggers.PLAYER_KILLED_ENTITY
         .createCriterion(new KilledTrigger$TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), Optional.empty()));
   }

   public static Criterion<KilledTrigger$TriggerInstance> playerKilledEntity(final EntityPredicate$Builder entity) {
      return CriteriaTriggers.PLAYER_KILLED_ENTITY
         .createCriterion(new KilledTrigger$TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), Optional.empty()));
   }

   public static Criterion<KilledTrigger$TriggerInstance> playerKilledEntity() {
      return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new KilledTrigger$TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
   }

   public static Criterion<KilledTrigger$TriggerInstance> playerKilledEntity(
      final Optional<EntityPredicate> entity, final Optional<DamageSourcePredicate> killingBlow
   ) {
      return CriteriaTriggers.PLAYER_KILLED_ENTITY
         .createCriterion(new KilledTrigger$TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), killingBlow));
   }

   public static Criterion<KilledTrigger$TriggerInstance> playerKilledEntity(
      final EntityPredicate$Builder entity, final Optional<DamageSourcePredicate> killingBlow
   ) {
      return CriteriaTriggers.PLAYER_KILLED_ENTITY
         .createCriterion(new KilledTrigger$TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), killingBlow));
   }

   public static Criterion<KilledTrigger$TriggerInstance> playerKilledEntity(
      final Optional<EntityPredicate> entity, final DamageSourcePredicate$Builder killingBlow
   ) {
      return CriteriaTriggers.PLAYER_KILLED_ENTITY
         .createCriterion(new KilledTrigger$TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), Optional.of(killingBlow.build())));
   }

   public static Criterion<KilledTrigger$TriggerInstance> playerKilledEntity(
      final EntityPredicate$Builder entity, final DamageSourcePredicate$Builder killingBlow
   ) {
      return CriteriaTriggers.PLAYER_KILLED_ENTITY
         .createCriterion(new KilledTrigger$TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), Optional.of(killingBlow.build())));
   }

   public static Criterion<KilledTrigger$TriggerInstance> playerKilledEntityNearSculkCatalyst() {
      return CriteriaTriggers.KILL_MOB_NEAR_SCULK_CATALYST
         .createCriterion(new KilledTrigger$TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
   }

   public static Criterion<KilledTrigger$TriggerInstance> entityKilledPlayer(final Optional<EntityPredicate> entity) {
      return CriteriaTriggers.ENTITY_KILLED_PLAYER
         .createCriterion(new KilledTrigger$TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), Optional.empty()));
   }

   public static Criterion<KilledTrigger$TriggerInstance> entityKilledPlayer(final EntityPredicate$Builder entity) {
      return CriteriaTriggers.ENTITY_KILLED_PLAYER
         .createCriterion(new KilledTrigger$TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), Optional.empty()));
   }

   public static Criterion<KilledTrigger$TriggerInstance> entityKilledPlayer() {
      return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new KilledTrigger$TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
   }

   public static Criterion<KilledTrigger$TriggerInstance> entityKilledPlayer(
      final Optional<EntityPredicate> entity, final Optional<DamageSourcePredicate> killingBlow
   ) {
      return CriteriaTriggers.ENTITY_KILLED_PLAYER
         .createCriterion(new KilledTrigger$TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), killingBlow));
   }

   public static Criterion<KilledTrigger$TriggerInstance> entityKilledPlayer(
      final EntityPredicate$Builder entity, final Optional<DamageSourcePredicate> killingBlow
   ) {
      return CriteriaTriggers.ENTITY_KILLED_PLAYER
         .createCriterion(new KilledTrigger$TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), killingBlow));
   }

   public static Criterion<KilledTrigger$TriggerInstance> entityKilledPlayer(
      final Optional<EntityPredicate> entity, final DamageSourcePredicate$Builder killingBlow
   ) {
      return CriteriaTriggers.ENTITY_KILLED_PLAYER
         .createCriterion(new KilledTrigger$TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), Optional.of(killingBlow.build())));
   }

   public static Criterion<KilledTrigger$TriggerInstance> entityKilledPlayer(
      final EntityPredicate$Builder entity, final DamageSourcePredicate$Builder killingBlow
   ) {
      return CriteriaTriggers.ENTITY_KILLED_PLAYER
         .createCriterion(new KilledTrigger$TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), Optional.of(killingBlow.build())));
   }

   public boolean matches(final ServerPlayer player, final LootContext entity, final DamageSource killingBlow) {
      return this.killingBlow.isPresent() && !this.killingBlow.get().matches(player, killingBlow)
         ? false
         : this.entity.isEmpty() || this.entity.get().value().test(entity);
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.entityContext(), "entity", this.entity);
   }
}
