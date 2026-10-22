package net.minecraft.client.gui.components.toasts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MusicToastDisplayState;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class ToastManager$1 {
   static {
      try {
         $SwitchMap$net$minecraft$client$MusicToastDisplayState[MusicToastDisplayState.PAUSE.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$client$MusicToastDisplayState[MusicToastDisplayState.PAUSE_AND_TOAST.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$client$MusicToastDisplayState[MusicToastDisplayState.NEVER.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
