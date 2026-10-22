package net.minecraft.util;

import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Map;
import java.util.OptionalLong;
import org.apache.commons.io.IOUtils;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class HttpUtil {
   private static final Logger LOGGER = LogUtils.getLogger();

   private HttpUtil() {
   }

   public static Path downloadFile(
      final Path targetDir,
      final URL url,
      final Map<String, String> headers,
      final HashFunction hashFunction,
      @Nullable final HashCode requestedHash,
      final int maxSize,
      final Proxy proxy,
      final HttpUtil$DownloadProgressListener listener
   ) {
      HttpURLConnection connection = null;
      InputStream input = null;
      listener.requestStart();
      Path targetFile;
      if (requestedHash != null) {
         targetFile = cachedFilePath(targetDir, requestedHash);

         try {
            if (checkExistingFile(targetFile, hashFunction, requestedHash)) {
               LOGGER.info("Returning cached file since actual hash matches requested");
               listener.requestFinished(true);
               updateModificationTime(targetFile);
               return targetFile;
            }
         } catch (IOException var35) {
            LOGGER.warn("Failed to check cached file {}", targetFile, var35);
         }

         try {
            LOGGER.warn("Existing file {} not found or had mismatched hash", targetFile);
            Files.deleteIfExists(targetFile);
         } catch (IOException var34) {
            listener.requestFinished(false);
            throw new UncheckedIOException("Failed to remove existing file " + targetFile, var34);
         }
      } else {
         targetFile = null;
      }

      Path actualHash;
      try {
         connection = (HttpURLConnection)url.openConnection(proxy);
         connection.setInstanceFollowRedirects(true);
         headers.forEach(connection::setRequestProperty);
         input = connection.getInputStream();
         long contentLength = connection.getContentLengthLong();
         OptionalLong size = contentLength != -1L ? OptionalLong.of(contentLength) : OptionalLong.empty();
         FileUtil.createDirectoriesSafe(targetDir);
         listener.downloadStart(size);
         if (size.isPresent() && size.getAsLong() > maxSize) {
            throw new IOException("Filesize is bigger than maximum allowed (file is " + size + ", limit is " + maxSize + ")");
         }

         if (targetFile == null) {
            Path tmpPath = Files.createTempFile(targetDir, "download", ".tmp");

            try {
               HashCode actualHashx = downloadAndHash(hashFunction, maxSize, listener, input, tmpPath);
               Path actualPath = cachedFilePath(targetDir, actualHashx);
               if (!checkExistingFile(actualPath, hashFunction, actualHashx)) {
                  Files.move(tmpPath, actualPath, StandardCopyOption.REPLACE_EXISTING);
               } else {
                  updateModificationTime(actualPath);
               }

               listener.requestFinished(true);
               return actualPath;
            } finally {
               Files.deleteIfExists(tmpPath);
            }
         }

         HashCode actualHashx = downloadAndHash(hashFunction, maxSize, listener, input, targetFile);
         if (!actualHashx.equals(requestedHash)) {
            throw new IOException("Hash of downloaded file (" + actualHashx + ") did not match requested (" + requestedHash + ")");
         }

         listener.requestFinished(true);
         actualHash = targetFile;
      } catch (Throwable var36) {
         if (connection != null) {
            InputStream error = connection.getErrorStream();
            if (error != null) {
               try {
                  LOGGER.error("HTTP response error: {}", IOUtils.toString(error, StandardCharsets.UTF_8));
               } catch (Exception var32) {
                  LOGGER.error("Failed to read response from server");
               }
            }
         }

         listener.requestFinished(false);
         throw new IllegalStateException("Failed to download file " + url, var36);
      } finally {
         IOUtils.closeQuietly(input);
      }

      return actualHash;
   }

   private static void updateModificationTime(final Path targetFile) {
      try {
         Files.setLastModifiedTime(targetFile, FileTime.from(Instant.now()));
      } catch (IOException var2) {
         LOGGER.warn("Failed to update modification time of {}", targetFile, var2);
      }
   }

   private static HashCode hashFile(final Path file, final HashFunction hashFunction) throws IOException {
      Hasher hasher = hashFunction.newHasher();

      try (
         OutputStream outputStream = Funnels.asOutputStream(hasher);
         InputStream fileInput = Files.newInputStream(file);
      ) {
         fileInput.transferTo(outputStream);
      }

      return hasher.hash();
   }

   private static boolean checkExistingFile(final Path file, final HashFunction hashFunction, final HashCode expectedHash) throws IOException {
      if (Files.exists(file)) {
         HashCode actualHash = hashFile(file, hashFunction);
         if (actualHash.equals(expectedHash)) {
            return true;
         }

         LOGGER.warn("Mismatched hash of file {}, expected {} but found {}", new Object[]{file, expectedHash, actualHash});
      }

      return false;
   }

   private static Path cachedFilePath(final Path targetDir, final HashCode requestedHash) {
      return targetDir.resolve(requestedHash.toString());
   }

   private static HashCode downloadAndHash(
      final HashFunction hashFunction, final int maxSize, final HttpUtil$DownloadProgressListener listener, final InputStream input, final Path downloadFile
   ) throws IOException {
      HashCode var11;
      try (OutputStream output = Files.newOutputStream(downloadFile, StandardOpenOption.CREATE)) {
         Hasher hasher = hashFunction.newHasher();
         byte[] buffer = new byte[8196];
         long readSoFar = 0L;

         int read;
         while ((read = input.read(buffer)) >= 0) {
            readSoFar += read;
            listener.downloadedBytes(readSoFar);
            if (readSoFar > maxSize) {
               throw new IOException("Filesize was bigger than maximum allowed (got >= " + readSoFar + ", limit was " + maxSize + ")");
            }

            if (Thread.interrupted()) {
               LOGGER.error("INTERRUPTED");
               throw new IOException("Download interrupted");
            }

            output.write(buffer, 0, read);
            hasher.putBytes(buffer, 0, read);
         }

         var11 = hasher.hash();
      }

      return var11;
   }

   public static int getAvailablePort() {
      try {
         int var1;
         try (ServerSocket server = new ServerSocket(0)) {
            var1 = server.getLocalPort();
         }

         return var1;
      } catch (IOException var5) {
         return 25564;
      }
   }

   public static boolean isPortAvailable(final int port) {
      if (port >= 0 && port <= 65535) {
         try {
            boolean var2;
            try (ServerSocket server = new ServerSocket(port)) {
               var2 = server.getLocalPort() == port;
            }

            return var2;
         } catch (IOException var6) {
            return false;
         }
      } else {
         return false;
      }
   }
}
