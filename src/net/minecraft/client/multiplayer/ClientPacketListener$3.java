package net.minecraft.client.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket$Action;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class ClientPacketListener$3 {
   static {
      try {
         $SwitchMap$net$minecraft$network$protocol$game$ClientboundPlayerInfoUpdatePacket$Action[ClientboundPlayerInfoUpdatePacket$Action.INITIALIZE_CHAT
            .ordinal()] = 1;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ClientboundPlayerInfoUpdatePacket$Action[ClientboundPlayerInfoUpdatePacket$Action.UPDATE_GAME_MODE
            .ordinal()] = 2;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ClientboundPlayerInfoUpdatePacket$Action[ClientboundPlayerInfoUpdatePacket$Action.UPDATE_LISTED
            .ordinal()] = 3;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ClientboundPlayerInfoUpdatePacket$Action[ClientboundPlayerInfoUpdatePacket$Action.UPDATE_LATENCY
            .ordinal()] = 4;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ClientboundPlayerInfoUpdatePacket$Action[ClientboundPlayerInfoUpdatePacket$Action.UPDATE_DISPLAY_NAME
            .ordinal()] = 5;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ClientboundPlayerInfoUpdatePacket$Action[ClientboundPlayerInfoUpdatePacket$Action.UPDATE_HAT.ordinal()] = 6;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ClientboundPlayerInfoUpdatePacket$Action[ClientboundPlayerInfoUpdatePacket$Action.UPDATE_LIST_ORDER
            .ordinal()] = 7;
      } catch (NoSuchFieldError var1) {
      }
   }
}
