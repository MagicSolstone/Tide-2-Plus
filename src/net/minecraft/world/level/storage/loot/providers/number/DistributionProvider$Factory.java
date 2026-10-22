package net.minecraft.world.level.storage.loot.providers.number;

import net.minecraft.core.Holder;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.storage.loot.Validatable;

@FunctionalInterface
public interface DistributionProvider$Factory<Value extends Validatable, Self extends DistributionProvider<Value>> {
   Self create(WeightedList<Holder<Value>> distribution);
}
