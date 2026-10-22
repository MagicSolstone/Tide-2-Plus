package com.mojang.realmsclient.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class UploadResult$Builder {
   private int statusCode = -1;
   @Nullable
   private String errorMessage;

   public UploadResult$Builder withStatusCode(final int statusCode) {
      this.statusCode = statusCode;
      return this;
   }

   public UploadResult$Builder withErrorMessage(@Nullable final String errorMessage) {
      this.errorMessage = errorMessage;
      return this;
   }

   public UploadResult build() {
      return new UploadResult(this.statusCode, this.errorMessage);
   }
}
