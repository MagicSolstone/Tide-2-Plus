package net.minecraft.client.gui.screens.social;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.PresenceSharing;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class PresenceHandler$1 {
   static {
      try {
         $SwitchMap$net$minecraft$client$PresenceSharing[PresenceSharing.NONE.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$client$PresenceSharing[PresenceSharing.LIMITED.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$client$PresenceSharing[PresenceSharing.ALL.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
