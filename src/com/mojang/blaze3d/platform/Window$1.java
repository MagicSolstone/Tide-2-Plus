package com.mojang.blaze3d.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Util$OS;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class Window$1 {
   static {
      try {
         $SwitchMap$net$minecraft$util$Util$OS[Util$OS.WINDOWS.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$util$Util$OS[Util$OS.LINUX.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$util$Util$OS[Util$OS.SOLARIS.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$util$Util$OS[Util$OS.OSX.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }
   }
}
