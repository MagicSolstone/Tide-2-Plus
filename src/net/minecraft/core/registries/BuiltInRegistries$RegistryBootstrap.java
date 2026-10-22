package net.minecraft.core.registries;

import net.minecraft.core.Registry;

@FunctionalInterface
interface BuiltInRegistries$RegistryBootstrap<T> {
   Object run(Registry<T> registry);
}
