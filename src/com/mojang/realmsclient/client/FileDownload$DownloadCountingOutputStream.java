package com.mojang.realmsclient.client;

import com.mojang.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen$DownloadStatus;
import java.io.IOException;
import java.io.OutputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.io.output.CountingOutputStream;

@Environment(EnvType.CLIENT)
class FileDownload$DownloadCountingOutputStream extends CountingOutputStream {
   private final RealmsDownloadLatestWorldScreen$DownloadStatus downloadStatus;

   public FileDownload$DownloadCountingOutputStream(final OutputStream out, final RealmsDownloadLatestWorldScreen$DownloadStatus downloadStatus) {
      super(out);
      this.downloadStatus = downloadStatus;
   }

   protected void afterWrite(final int n) throws IOException {
      super.afterWrite(n);
      this.downloadStatus.bytesWritten = this.getByteCount();
   }
}
