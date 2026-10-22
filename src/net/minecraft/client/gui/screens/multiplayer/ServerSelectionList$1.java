package net.minecraft.client.gui.screens.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ServerData$State;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class ServerSelectionList$1 {
   static {
      try {
         $SwitchMap$net$minecraft$client$multiplayer$ServerData$State[ServerData$State.INITIAL.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$client$multiplayer$ServerData$State[ServerData$State.PINGING.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$client$multiplayer$ServerData$State[ServerData$State.INCOMPATIBLE.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$client$multiplayer$ServerData$State[ServerData$State.UNREACHABLE.ordinal()] = 4;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$client$multiplayer$ServerData$State[ServerData$State.SUCCESSFUL.ordinal()] = 5;
      } catch (NoSuchFieldError var1) {
      }
   }
}
