package net.minecraft.world.level.storage.loot.providers.number;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.Validatable;

@FunctionalInterface
public interface BinaryProvider$Factory<Value extends Validatable, Self extends BinaryProvider<Value>> {
   Self create(Holder<Value> left, Holder<Value> right);
}
