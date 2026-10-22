package net.minecraft.client.gui.screens.dialog;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.dialog.DialogAction;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class DialogScreen$1 {
   static {
      try {
         $SwitchMap$net$minecraft$server$dialog$DialogAction[DialogAction.NONE.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$server$dialog$DialogAction[DialogAction.CLOSE.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$server$dialog$DialogAction[DialogAction.WAIT_FOR_RESPONSE.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
