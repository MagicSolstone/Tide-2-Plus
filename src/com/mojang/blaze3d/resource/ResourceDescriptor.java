package com.mojang.blaze3d.resource;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ResourceDescriptor<T> {
   T allocate();

   default void prepare(final T resource) {
   }

   void free(T resource);

   default boolean canUsePhysicalResource(final ResourceDescriptor<?> other) {
      return this.equals(other);
   }
}
