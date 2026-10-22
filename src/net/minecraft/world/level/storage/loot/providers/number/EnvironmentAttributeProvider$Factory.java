package net.minecraft.world.level.storage.loot.providers.number;

import net.minecraft.world.attribute.EnvironmentAttribute;

@FunctionalInterface
public interface EnvironmentAttributeProvider$Factory<Self extends EnvironmentAttributeProvider> {
   Self create(EnvironmentAttribute<?> attribute);
}
