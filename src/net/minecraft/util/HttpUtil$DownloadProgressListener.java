package net.minecraft.util;

import java.util.OptionalLong;

public interface HttpUtil$DownloadProgressListener {
   void requestStart();

   void downloadStart(OptionalLong sizeBytes);

   void downloadedBytes(long bytesSoFar);

   void requestFinished(boolean success);
}
