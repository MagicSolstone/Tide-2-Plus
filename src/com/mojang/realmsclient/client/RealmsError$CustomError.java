package com.mojang.realmsclient.client;

import com.mojang.realmsclient.exception.RealmsHttpException;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record RealmsError$CustomError(int httpCode, @Nullable Component payload) implements RealmsError {
   public static final RealmsError$CustomError SERVICE_BUSY = new RealmsError$CustomError(429, Component.translatable("mco.errorMessage.serviceBusy"));
   public static final Component RETRY_MESSAGE = Component.translatable("mco.errorMessage.retry");
   public static final String BODY_TAG = "<body>";
   public static final String CLOSING_BODY_TAG = "</body>";

   public static RealmsError$CustomError unknownCompatibilityResponse(final String response) {
      return new RealmsError$CustomError(500, Component.translatable("mco.errorMessage.realmsService.unknownCompatibility", response));
   }

   public static RealmsError$CustomError configurationError() {
      return new RealmsError$CustomError(500, Component.translatable("mco.errorMessage.realmsService.configurationError"));
   }

   public static RealmsError$CustomError connectivityError(final RealmsHttpException exception) {
      return new RealmsError$CustomError(500, Component.translatable("mco.errorMessage.realmsService.connectivity", exception.getMessage()));
   }

   public static RealmsError$CustomError retry(final int statusCode) {
      return new RealmsError$CustomError(statusCode, RETRY_MESSAGE);
   }

   public static RealmsError$CustomError noPayload(final int statusCode) {
      return new RealmsError$CustomError(statusCode, null);
   }

   public static RealmsError$CustomError htmlPayload(final int statusCode, final String payload) {
      int bodyStart = payload.indexOf("<body>");
      int bodyEnd = payload.indexOf("</body>");
      if (bodyStart >= 0 && bodyEnd > bodyStart) {
         return new RealmsError$CustomError(statusCode, Component.literal(payload.substring(bodyStart + "<body>".length(), bodyEnd).trim()));
      } else {
         LOGGER.error("Got an error with an unreadable html body {}", payload);
         return new RealmsError$CustomError(statusCode, null);
      }
   }

   @Override
   public int errorCode() {
      return this.httpCode;
   }

   @Override
   public Component errorMessage() {
      return this.payload != null ? this.payload : NO_MESSAGE;
   }

   @Override
   public String logMessage() {
      return this.payload != null
         ? String.format(Locale.ROOT, "Realms service error (%d) with message '%s'", this.httpCode, this.payload.getString())
         : String.format(Locale.ROOT, "Realms service error (%d) with no payload", this.httpCode);
   }
}
