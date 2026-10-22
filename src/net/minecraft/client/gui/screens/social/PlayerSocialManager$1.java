package net.minecraft.client.gui.screens.social;

import com.mojang.authlib.services.FriendsService.ResultCode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class PlayerSocialManager$1 {
   static {
      try {
         $SwitchMap$com$mojang$authlib$services$FriendsService$ResultCode[ResultCode.TOO_MANY_REQUESTS.ordinal()] = 1;
      } catch (NoSuchFieldError var11) {
      }

      try {
         $SwitchMap$com$mojang$authlib$services$FriendsService$ResultCode[ResultCode.UNKNOWN_PROFILE.ordinal()] = 2;
      } catch (NoSuchFieldError var10) {
      }

      try {
         $SwitchMap$com$mojang$authlib$services$FriendsService$ResultCode[ResultCode.UNAUTHORIZED.ordinal()] = 3;
      } catch (NoSuchFieldError var9) {
      }

      try {
         $SwitchMap$com$mojang$authlib$services$FriendsService$ResultCode[ResultCode.FORBIDDEN.ordinal()] = 4;
      } catch (NoSuchFieldError var8) {
      }

      try {
         $SwitchMap$com$mojang$authlib$services$FriendsService$ResultCode[ResultCode.SERVICE_NOT_AVAILABLE.ordinal()] = 5;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$com$mojang$authlib$services$FriendsService$ResultCode[ResultCode.ERROR.ordinal()] = 6;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$com$mojang$authlib$services$FriendsService$ResultCode[ResultCode.SUCCESS.ordinal()] = 7;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$com$mojang$authlib$services$FriendsService$ResultCode[ResultCode.UPGRADE_NEEDED.ordinal()] = 8;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$com$mojang$authlib$services$FriendsService$ResultCode[ResultCode.CONNECTION_ISSUE.ordinal()] = 9;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$com$mojang$authlib$services$FriendsService$ResultCode[ResultCode.TEMPORARY_UNAVAILABLE.ordinal()] = 10;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$com$mojang$authlib$services$FriendsService$ResultCode[ResultCode.GENERIC_ERROR.ordinal()] = 11;
      } catch (NoSuchFieldError var1) {
      }
   }
}
