package net.minecraft.client.gui.font;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font$DisplayMode;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class GlyphRenderTypes$1 {
   static {
      try {
         $SwitchMap$net$minecraft$client$gui$Font$DisplayMode[Font$DisplayMode.NORMAL.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$client$gui$Font$DisplayMode[Font$DisplayMode.SEE_THROUGH.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$client$gui$Font$DisplayMode[Font$DisplayMode.POLYGON_OFFSET.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
