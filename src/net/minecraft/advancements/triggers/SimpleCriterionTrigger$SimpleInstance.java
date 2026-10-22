package net.minecraft.advancements.triggers;

import java.util.Optional;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public interface SimpleCriterionTrigger$SimpleInstance extends CriterionTriggerInstance {
   @Override
   default void validate(final ValidationContextSource validator) {
      Validatable.validateHolder(validator.entityContext(), "player", this.player());
   }

   Optional<Holder<LootItemCondition>> player();
}
