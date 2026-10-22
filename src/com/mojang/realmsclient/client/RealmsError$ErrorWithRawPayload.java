package com.mojang.realmsclient.client;

import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public record RealmsError$ErrorWithRawPayload(int httpCode, String payload) implements RealmsError {
   @Override
   public int errorCode() {
      return this.httpCode;
   }

   @Override
   public Component errorMessage() {
      return Component.literal(this.payload);
   }

   @Override
   public String logMessage() {
      return String.format(Locale.ROOT, "Realms service error (%d) with raw payload '%s'", this.httpCode, this.payload);
   }
}
