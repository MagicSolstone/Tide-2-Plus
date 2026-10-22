package com.mojang.realmsclient.client.worldupload;

import com.mojang.realmsclient.client.UploadStatus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class RealmsWorldUploadStatusTracker$1 implements RealmsWorldUploadStatusTracker {
   private final UploadStatus uploadStatus = new UploadStatus();

   @Override
   public UploadStatus getUploadStatus() {
      return this.uploadStatus;
   }

   @Override
   public void setUploading() {
   }
}
