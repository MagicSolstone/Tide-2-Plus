package net.minecraft.world.level.storage.loot.providers.number;

import net.minecraft.core.HolderSet;
import net.minecraft.world.level.storage.loot.Validatable;

@FunctionalInterface
public interface AggregateProvider$Factory<Value extends Validatable, Self extends AggregateProvider<Value>> {
   Self create(HolderSet<Value> value);
}
