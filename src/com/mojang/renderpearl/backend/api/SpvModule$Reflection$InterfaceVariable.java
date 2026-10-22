package com.mojang.renderpearl.backend.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface SpvModule$Reflection$InterfaceVariable {
   String name();

   SpvModule$Reflection$Type type();

   int location();

   void location(int location);

   default boolean hasDecoration(final int decoration) {
      return this.decoration(decoration) != 0;
   }

   int decoration(int decoration);
}
