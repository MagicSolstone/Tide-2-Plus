package net.minecraft.client.gui.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class GuiItemAtlas$1 {
   static {
      try {
         $SwitchMap$net$minecraft$client$gui$render$DynamicAtlasAllocator$SlotState[DynamicAtlasAllocator$SlotState.EMPTY.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$client$gui$render$DynamicAtlasAllocator$SlotState[DynamicAtlasAllocator$SlotState.STALE.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$client$gui$render$DynamicAtlasAllocator$SlotState[DynamicAtlasAllocator$SlotState.READY.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
