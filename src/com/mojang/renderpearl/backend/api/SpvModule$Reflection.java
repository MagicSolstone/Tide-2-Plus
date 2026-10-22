package com.mojang.renderpearl.backend.api;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface SpvModule$Reflection {
   List<SpvModule$Reflection$InterfaceVariable> inputs();

   List<SpvModule$Reflection$InterfaceVariable> outputs();

   List<SpvModule$Reflection$Descriptor> descriptors(int resourceType);

   List<SpvModule$Reflection$Descriptor> descriptors();

   List<SpvModule$Reflection$PushConstant> pushConstants();
}
