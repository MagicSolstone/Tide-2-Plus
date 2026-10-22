package net.minecraft.client.multiplayer.chat.report;

import com.mojang.authlib.exceptions.MinecraftClientException.ErrorType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class AbuseReportSender$1 {
   static {
      try {
         $SwitchMap$com$mojang$authlib$exceptions$MinecraftClientException$ErrorType[ErrorType.SERVICE_UNAVAILABLE.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$com$mojang$authlib$exceptions$MinecraftClientException$ErrorType[ErrorType.HTTP_ERROR.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$com$mojang$authlib$exceptions$MinecraftClientException$ErrorType[ErrorType.JSON_ERROR.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
