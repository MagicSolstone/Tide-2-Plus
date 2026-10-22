package net.minecraft.client.gui.screens.friends;

import com.mojang.authlib.services.response.PresenceStatus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class FriendEntry$1 {
   static {
      try {
         $SwitchMap$com$mojang$authlib$services$response$PresenceStatus[PresenceStatus.PLAYING_HOSTED_SERVER.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$com$mojang$authlib$services$response$PresenceStatus[PresenceStatus.PLAYING_SERVER.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$com$mojang$authlib$services$response$PresenceStatus[PresenceStatus.PLAYING_REALMS.ordinal()] = 3;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$com$mojang$authlib$services$response$PresenceStatus[PresenceStatus.PLAYING_OFFLINE.ordinal()] = 4;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$com$mojang$authlib$services$response$PresenceStatus[PresenceStatus.ONLINE.ordinal()] = 5;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$com$mojang$authlib$services$response$PresenceStatus[PresenceStatus.OFFLINE.ordinal()] = 6;
      } catch (NoSuchFieldError var1) {
      }
   }
}
