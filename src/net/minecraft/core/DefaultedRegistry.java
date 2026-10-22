package net.minecraft.core;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface DefaultedRegistry<T> extends Registry<T> {
   @NonNull
   @Override
   Identifier getKey(T thing);

   @NonNull
   @Override
   T getValue(@Nullable Identifier key);

   @NonNull
   @Override
   T byId(int id);

   Identifier getDefaultKey();
}
