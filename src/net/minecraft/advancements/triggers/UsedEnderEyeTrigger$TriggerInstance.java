package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.MinMaxBounds$Doubles;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record UsedEnderEyeTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player, MinMaxBounds$Doubles distance)
   implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<UsedEnderEyeTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(UsedEnderEyeTrigger$TriggerInstance::player),
            MinMaxBounds$Doubles.CODEC.optionalFieldOf("distance", MinMaxBounds$Doubles.ANY).forGetter(UsedEnderEyeTrigger$TriggerInstance::distance)
         )
         .apply(i, UsedEnderEyeTrigger$TriggerInstance::new)
   );

   public boolean matches(final double sqrDistance) {
      return this.distance.matchesSqr(sqrDistance);
   }
}
