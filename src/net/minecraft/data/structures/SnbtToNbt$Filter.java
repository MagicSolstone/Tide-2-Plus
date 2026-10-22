package net.minecraft.data.structures;

import net.minecraft.nbt.CompoundTag;

@FunctionalInterface
public interface SnbtToNbt$Filter {
   CompoundTag apply(final String name, final CompoundTag input);
}
