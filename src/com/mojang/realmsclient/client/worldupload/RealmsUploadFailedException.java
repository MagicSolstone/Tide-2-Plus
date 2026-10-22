package com.mojang.realmsclient.client.worldupload;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class RealmsUploadFailedException extends RealmsUploadException {
   private final Component errorMessage;

   public RealmsUploadFailedException(final Component errorMessage) {
      this.errorMessage = errorMessage;
   }

   public RealmsUploadFailedException(final String errorMessage) {
      this(Component.literal(errorMessage));
   }

   @Override
   public Component getStatusMessage() {
      return Component.translatable("mco.upload.failed", this.errorMessage);
   }
}
