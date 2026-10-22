package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate$Builder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record ChanneledLightningTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player, List<Holder<LootItemCondition>> victims)
   implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<ChanneledLightningTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(ChanneledLightningTrigger$TriggerInstance::player),
            LootItemCondition.CODEC.listOf().optionalFieldOf("victims", List.of()).forGetter(ChanneledLightningTrigger$TriggerInstance::victims)
         )
         .apply(i, ChanneledLightningTrigger$TriggerInstance::new)
   );

   public static Criterion<ChanneledLightningTrigger$TriggerInstance> channeledLightning(final EntityPredicate$Builder... victims) {
      return CriteriaTriggers.CHANNELED_LIGHTNING
         .createCriterion(new ChanneledLightningTrigger$TriggerInstance(Optional.empty(), EntityPredicate.wrap(victims)));
   }

   public boolean matches(final Collection<? extends LootContext> victims) {
      for (Holder<LootItemCondition> predicate : this.victims) {
         boolean found = false;

         for (LootContext victim : victims) {
            if (predicate.value().test(victim)) {
               found = true;
               break;
            }
         }

         if (!found) {
            return false;
         }
      }

      return true;
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.entityContext(), "victims", this.victims);
   }
}
