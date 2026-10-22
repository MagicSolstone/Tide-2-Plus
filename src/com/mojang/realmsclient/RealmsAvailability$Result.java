package com.mojang.realmsclient;

import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.screens.RealmsClientOutdatedScreen;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.gui.screens.RealmsParentalConsentScreen;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record RealmsAvailability$Result(RealmsAvailability$Type type, @Nullable RealmsServiceException exception) {
   public RealmsAvailability$Result(final RealmsAvailability$Type type) {
      this(type, null);
   }

   public RealmsAvailability$Result(final RealmsServiceException exception) {
      this(RealmsAvailability$Type.UNEXPECTED_ERROR, exception);
   }

   @Nullable
   public Screen createErrorScreen(final Screen lastScreen) {
      return (Screen)(switch (this.type) {
         case SUCCESS -> null;
         case INCOMPATIBLE_CLIENT -> new RealmsClientOutdatedScreen(lastScreen);
         case NEEDS_PARENTAL_CONSENT -> new RealmsParentalConsentScreen(lastScreen);
         case AUTHENTICATION_ERROR -> new RealmsGenericErrorScreen(
            Component.translatable("mco.error.invalid.session.title"), Component.translatable("mco.error.invalid.session.message"), lastScreen
         );
         case UNEXPECTED_ERROR -> new RealmsGenericErrorScreen(Objects.requireNonNull(this.exception), lastScreen);
      });
   }
}
