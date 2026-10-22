package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record DefaultBlockInteractionTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<Holder<LootItemCondition>> location)
   implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<DefaultBlockInteractionTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(DefaultBlockInteractionTrigger$TriggerInstance::player),
            LootItemCondition.CODEC.optionalFieldOf("location").forGetter(DefaultBlockInteractionTrigger$TriggerInstance::location)
         )
         .apply(i, DefaultBlockInteractionTrigger$TriggerInstance::new)
   );

   public boolean matches(final LootContext locationContext) {
      return this.location.isEmpty() || this.location.get().value().test(locationContext);
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.context(LootContextParamSets.BLOCK_USE), "location", this.location);
   }
}
