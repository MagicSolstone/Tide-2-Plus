package net.minecraft.data.worldgen;

import net.minecraft.core.Holder$Reference;
import net.minecraft.resources.ResourceKey;

public interface BootstrapContext<T> extends BootstrapContextAccess {
   Holder$Reference<T> register(ResourceKey<T> key, T value);
}
