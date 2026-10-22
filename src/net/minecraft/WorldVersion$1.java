package net.minecraft;

import net.minecraft.server.packs.PackType;

// $VF: synthetic class
class WorldVersion$1 {
   static {
      try {
         $SwitchMap$net$minecraft$server$packs$PackType[PackType.CLIENT_RESOURCES.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$server$packs$PackType[PackType.SERVER_DATA.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
