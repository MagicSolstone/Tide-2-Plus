package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate$Builder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record StartRidingTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<StartRidingTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(LootItemCondition.CODEC.optionalFieldOf("player").forGetter(StartRidingTrigger$TriggerInstance::player))
         .apply(i, StartRidingTrigger$TriggerInstance::new)
   );

   public static Criterion<StartRidingTrigger$TriggerInstance> playerStartsRiding(final EntityPredicate$Builder player) {
      return CriteriaTriggers.START_RIDING_TRIGGER.createCriterion(new StartRidingTrigger$TriggerInstance(Optional.of(EntityPredicate.wrap(player))));
   }
}
