package com.mojang.realmsclient.client;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.LenientJsonParser;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public interface RealmsError {
   Component NO_MESSAGE = Component.translatable("mco.errorMessage.noDetails");
   Logger LOGGER = LogUtils.getLogger();

   int errorCode();

   Component errorMessage();

   String logMessage();

   static RealmsError parse(final int httpCode, final String payload) {
      if (httpCode == 429) {
         return RealmsError$CustomError.SERVICE_BUSY;
      } else if (Strings.isNullOrEmpty(payload)) {
         return RealmsError$CustomError.noPayload(httpCode);
      } else {
         try {
            JsonObject object = LenientJsonParser.parse(payload).getAsJsonObject();
            String errorReason = GsonHelper.getAsString(object, "reason", null);
            String errorMessage = GsonHelper.getAsString(object, "errorMsg", null);
            int errorCode = GsonHelper.getAsInt(object, "errorCode", -1);
            if (errorMessage != null || errorReason != null || errorCode != -1) {
               return new RealmsError$ErrorWithJsonPayload(httpCode, errorCode != -1 ? errorCode : httpCode, errorReason, errorMessage);
            }
         } catch (Exception var6) {
            LOGGER.error("Could not parse RealmsError", var6);
         }

         return new RealmsError$ErrorWithRawPayload(httpCode, payload);
      }
   }
}
