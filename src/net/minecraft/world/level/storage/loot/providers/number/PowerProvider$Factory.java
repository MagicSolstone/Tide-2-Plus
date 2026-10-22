package net.minecraft.world.level.storage.loot.providers.number;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.Validatable;

@FunctionalInterface
public interface PowerProvider$Factory<Value extends Validatable, Self extends PowerProvider<Value>> {
   Self create(Holder<Value> left, Holder<Value> right);
}
