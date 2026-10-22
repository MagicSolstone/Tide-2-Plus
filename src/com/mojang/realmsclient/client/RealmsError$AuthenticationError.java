package com.mojang.realmsclient.client;

import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public record RealmsError$AuthenticationError(String message) implements RealmsError {
   public static final int ERROR_CODE = 401;

   @Override
   public int errorCode() {
      return 401;
   }

   @Override
   public Component errorMessage() {
      return Component.literal(this.message);
   }

   @Override
   public String logMessage() {
      return String.format(Locale.ROOT, "Realms authentication error with message '%s'", this.message);
   }
}
