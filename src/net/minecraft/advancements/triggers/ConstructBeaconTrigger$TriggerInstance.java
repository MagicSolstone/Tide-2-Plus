package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.MinMaxBounds$Ints;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record ConstructBeaconTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player, MinMaxBounds$Ints level)
   implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<ConstructBeaconTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(ConstructBeaconTrigger$TriggerInstance::player),
            MinMaxBounds$Ints.CODEC.optionalFieldOf("level", MinMaxBounds$Ints.ANY).forGetter(ConstructBeaconTrigger$TriggerInstance::level)
         )
         .apply(i, ConstructBeaconTrigger$TriggerInstance::new)
   );

   public static Criterion<ConstructBeaconTrigger$TriggerInstance> constructedBeacon() {
      return CriteriaTriggers.CONSTRUCT_BEACON.createCriterion(new ConstructBeaconTrigger$TriggerInstance(Optional.empty(), MinMaxBounds$Ints.ANY));
   }

   public static Criterion<ConstructBeaconTrigger$TriggerInstance> constructedBeacon(final MinMaxBounds$Ints level) {
      return CriteriaTriggers.CONSTRUCT_BEACON.createCriterion(new ConstructBeaconTrigger$TriggerInstance(Optional.empty(), level));
   }

   public boolean matches(final int levels) {
      return this.level.matches(levels);
   }
}
