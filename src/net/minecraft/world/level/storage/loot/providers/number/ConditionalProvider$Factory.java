package net.minecraft.world.level.storage.loot.providers.number;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

@FunctionalInterface
public interface ConditionalProvider$Factory<Value extends Validatable, Self extends ConditionalProvider<Value>> {
   Self create(Holder<LootItemCondition> condition, Holder<Value> onTrue, Holder<Value> onFalse);
}
