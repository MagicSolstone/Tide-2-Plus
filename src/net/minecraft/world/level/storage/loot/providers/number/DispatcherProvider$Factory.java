package net.minecraft.world.level.storage.loot.providers.number;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.Validatable;

@FunctionalInterface
public interface DispatcherProvider$Factory<Value extends Validatable, Self extends DispatcherProvider<Value>> {
   Self create(List<DispatcherProvider$Case<Value>> cases, Holder<Value> defaultValue);
}
