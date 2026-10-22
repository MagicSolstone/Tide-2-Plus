package com.mojang.renderpearl.backend.vulkan;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface DestructionQueue$Destroyer<T> {
   default void begin(final int count) {
   }

   void destroy(T t);

   default void end() {
   }
}
