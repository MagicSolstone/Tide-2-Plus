package net.minecraft.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ServerData$ServerPackStatus;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class ConnectScreen$2 {
   static {
      try {
         $SwitchMap$net$minecraft$client$multiplayer$ServerData$ServerPackStatus[ServerData$ServerPackStatus.ENABLED.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$client$multiplayer$ServerData$ServerPackStatus[ServerData$ServerPackStatus.DISABLED.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$client$multiplayer$ServerData$ServerPackStatus[ServerData$ServerPackStatus.PROMPT.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
