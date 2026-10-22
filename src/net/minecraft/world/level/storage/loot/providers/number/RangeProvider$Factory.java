package net.minecraft.world.level.storage.loot.providers.number;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.Validatable;

@FunctionalInterface
public interface RangeProvider$Factory<Value extends Validatable, Self extends RangeProvider<Value>> {
   Self create(Holder<Value> min, Holder<Value> max);
}
