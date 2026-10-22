package net.minecraft.client.gui.navigation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class ScreenPosition$1 {
   static {
      try {
         $SwitchMap$net$minecraft$client$gui$navigation$ScreenDirection[ScreenDirection.DOWN.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$client$gui$navigation$ScreenDirection[ScreenDirection.UP.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$client$gui$navigation$ScreenDirection[ScreenDirection.LEFT.ordinal()] = 3;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$client$gui$navigation$ScreenDirection[ScreenDirection.RIGHT.ordinal()] = 4;
      } catch (NoSuchFieldError var3) {
      }

      $SwitchMap$net$minecraft$client$gui$navigation$ScreenAxis = new int[ScreenAxis.values().length];

      try {
         $SwitchMap$net$minecraft$client$gui$navigation$ScreenAxis[ScreenAxis.HORIZONTAL.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$client$gui$navigation$ScreenAxis[ScreenAxis.VERTICAL.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
