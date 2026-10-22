package com.mojang.realmsclient.client.worldupload;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class RealmsUploadException extends RuntimeException {
   @Nullable
   public Component getStatusMessage() {
      return null;
   }

   @Nullable
   public Component[] getErrorMessages() {
      return null;
   }
}
