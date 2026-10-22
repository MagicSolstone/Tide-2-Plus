package net.minecraft.core;

import net.minecraft.resources.ResourceKey;

public interface Registry$PendingTags<T> {
   ResourceKey<? extends Registry<? extends T>> key();

   HolderLookup$RegistryLookup<T> lookup();

   void apply();

   int size();
}
