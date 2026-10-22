package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record CuredZombieVillagerTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player, Optional<Holder<LootItemCondition>> zombie, Optional<Holder<LootItemCondition>> villager
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<CuredZombieVillagerTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(CuredZombieVillagerTrigger$TriggerInstance::player),
            LootItemCondition.CODEC.optionalFieldOf("zombie").forGetter(CuredZombieVillagerTrigger$TriggerInstance::zombie),
            LootItemCondition.CODEC.optionalFieldOf("villager").forGetter(CuredZombieVillagerTrigger$TriggerInstance::villager)
         )
         .apply(i, CuredZombieVillagerTrigger$TriggerInstance::new)
   );

   public static Criterion<CuredZombieVillagerTrigger$TriggerInstance> curedZombieVillager() {
      return CriteriaTriggers.CURED_ZOMBIE_VILLAGER
         .createCriterion(new CuredZombieVillagerTrigger$TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
   }

   public boolean matches(final LootContext zombie, final LootContext villager) {
      return this.zombie.isPresent() && !this.zombie.get().value().test(zombie)
         ? false
         : !this.villager.isPresent() || this.villager.get().value().test(villager);
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.entityContext(), "zombie", this.zombie);
      Validatable.validateHolder(validator.entityContext(), "villager", this.villager);
   }
}
