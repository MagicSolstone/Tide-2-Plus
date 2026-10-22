package com.mojang.realmsclient.client;

import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record RealmsError$ErrorWithJsonPayload(int httpCode, int code, @Nullable String reason, @Nullable String message) implements RealmsError {
   @Override
   public int errorCode() {
      return this.code;
   }

   @Override
   public Component errorMessage() {
      String codeTranslationKey = "mco.errorMessage." + this.code;
      if (Language.getInstance().has(codeTranslationKey)) {
         return Component.translatable(codeTranslationKey);
      } else {
         if (this.reason != null) {
            String reasonTranslationKey = "mco.errorReason." + this.reason;
            if (Language.getInstance().has(reasonTranslationKey)) {
               return Component.translatable(reasonTranslationKey);
            }
         }

         return (Component)(this.message != null ? Component.literal(this.message) : NO_MESSAGE);
      }
   }

   @Override
   public String logMessage() {
      return String.format(Locale.ROOT, "Realms service error (%d/%d/%s) with message '%s'", this.httpCode, this.code, this.reason, this.message);
   }
}
