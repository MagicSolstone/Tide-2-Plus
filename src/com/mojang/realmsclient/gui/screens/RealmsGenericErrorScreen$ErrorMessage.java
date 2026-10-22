package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsError;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
record RealmsGenericErrorScreen$ErrorMessage(Component title, Component detail) {
   private static RealmsGenericErrorScreen$ErrorMessage forServiceError(final RealmsServiceException realmsServiceException) {
      RealmsError errorDetails = realmsServiceException.realmsError;
      return new RealmsGenericErrorScreen$ErrorMessage(
         Component.translatable("mco.errorMessage.realmsService.realmsError", errorDetails.errorCode()), errorDetails.errorMessage()
      );
   }
}
