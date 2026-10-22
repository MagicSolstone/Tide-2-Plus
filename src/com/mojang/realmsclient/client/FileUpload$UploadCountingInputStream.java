package com.mojang.realmsclient.client;

import java.io.IOException;
import java.io.InputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.io.input.CountingInputStream;

@Environment(EnvType.CLIENT)
class FileUpload$UploadCountingInputStream extends CountingInputStream {
   private final UploadStatus uploadStatus;

   private FileUpload$UploadCountingInputStream(final InputStream proxy, final UploadStatus uploadStatus) {
      super(proxy);
      this.uploadStatus = uploadStatus;
   }

   protected void afterRead(final int n) throws IOException {
      super.afterRead(n);
      this.uploadStatus.onWrite(this.getByteCount());
   }
}
