package net.minecraft.client.input;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Util$OS;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class InputQuirks$1 {
   static {
      try {
         $SwitchMap$net$minecraft$util$Util$OS[Util$OS.OSX.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$util$Util$OS[Util$OS.WINDOWS.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$util$Util$OS[Util$OS.LINUX.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
