package net.minecraft.core.component;

import java.util.function.BiConsumer;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface DataComponentInitializers$PendingComponents<T> {
   ResourceKey<? extends Registry<? extends T>> key();

   void forEach(BiConsumer<Holder$Reference<T>, DataComponentMap> output);

   void apply();
}
