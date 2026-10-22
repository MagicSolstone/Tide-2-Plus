package net.minecraft.world.level.storage.loot.providers.number;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.Validatable;

@FunctionalInterface
public interface UnaryProvider$Factory<Value extends Validatable, Self extends UnaryProvider<Value>> {
   Self create(Holder<Value> input);
}
