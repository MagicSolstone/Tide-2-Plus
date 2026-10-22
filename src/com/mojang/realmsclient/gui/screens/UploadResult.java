package com.mojang.realmsclient.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record UploadResult(int statusCode, @Nullable String errorMessage) {
   @Nullable
   public String getSimplifiedErrorMessage() {
      if (this.statusCode >= 200 && this.statusCode < 300) {
         return null;
      } else {
         return this.statusCode == 400 && this.errorMessage != null ? this.errorMessage : String.valueOf(this.statusCode);
      }
   }
}
