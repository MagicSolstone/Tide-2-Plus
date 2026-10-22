package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.MobEffectsPredicate;
import net.minecraft.advancements.predicates.MobEffectsPredicate$Builder;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate$Builder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jspecify.annotations.Nullable;

public record EffectsChangedTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player, Optional<MobEffectsPredicate> effects, Optional<Holder<LootItemCondition>> source
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<EffectsChangedTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(EffectsChangedTrigger$TriggerInstance::player),
            MobEffectsPredicate.CODEC.optionalFieldOf("effects").forGetter(EffectsChangedTrigger$TriggerInstance::effects),
            LootItemCondition.CODEC.optionalFieldOf("source").forGetter(EffectsChangedTrigger$TriggerInstance::source)
         )
         .apply(i, EffectsChangedTrigger$TriggerInstance::new)
   );

   public static Criterion<EffectsChangedTrigger$TriggerInstance> hasEffects(final MobEffectsPredicate$Builder effects) {
      return CriteriaTriggers.EFFECTS_CHANGED
         .createCriterion(new EffectsChangedTrigger$TriggerInstance(Optional.empty(), Optional.of(effects.build()), Optional.empty()));
   }

   public static Criterion<EffectsChangedTrigger$TriggerInstance> gotEffectsFrom(final EntityPredicate$Builder source) {
      return CriteriaTriggers.EFFECTS_CHANGED
         .createCriterion(new EffectsChangedTrigger$TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(EntityPredicate.wrap(source.build()))));
   }

   public boolean matches(final ServerPlayer player, @Nullable final LootContext source) {
      return this.effects.isPresent() && !this.effects.get().matches((LivingEntity)player)
         ? false
         : !this.source.isPresent() || source != null && this.source.get().value().test(source);
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.entityContext(), "source", this.source);
   }
}
