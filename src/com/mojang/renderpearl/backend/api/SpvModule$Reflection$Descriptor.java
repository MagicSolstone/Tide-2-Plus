package com.mojang.renderpearl.backend.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface SpvModule$Reflection$Descriptor {
   String name();

   SpvModule$Reflection$Type type();

   int resourceType();

   int descriptorSetIndex();

   void descriptorSetIndex(int index);

   int binding();

   void binding(int location);
}
