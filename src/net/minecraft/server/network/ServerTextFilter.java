package net.minecraft.server.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.FilterMask;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.LenientJsonParser;
import net.minecraft.util.StringUtil;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public abstract class ServerTextFilter implements AutoCloseable {
   protected static final Logger LOGGER = LogUtils.getLogger();
   private static final AtomicInteger WORKER_COUNT = new AtomicInteger(1);
   private static final ThreadFactory THREAD_FACTORY = runnable -> new Thread(runnable, "Chat-Filter-Worker-" + WORKER_COUNT.getAndIncrement());
   private final URL chatEndpoint;
   private final ServerTextFilter$MessageEncoder chatEncoder;
   private final ServerTextFilter$IgnoreStrategy chatIgnoreStrategy;
   private final ExecutorService workerPool;

   protected static ExecutorService createWorkerPool(final int maxConcurrentRequests) {
      return Executors.newFixedThreadPool(maxConcurrentRequests, THREAD_FACTORY);
   }

   protected ServerTextFilter(
      final URL chatEndpoint,
      final ServerTextFilter$MessageEncoder chatEncoder,
      final ServerTextFilter$IgnoreStrategy chatIgnoreStrategy,
      final ExecutorService workerPool
   ) {
      this.chatIgnoreStrategy = chatIgnoreStrategy;
      this.workerPool = workerPool;
      this.chatEndpoint = chatEndpoint;
      this.chatEncoder = chatEncoder;
   }

   protected static URL getEndpoint(final URI host, @Nullable final JsonObject source, final String id, final String def) throws MalformedURLException {
      String endpointConfig = getEndpointFromConfig(source, id, def);
      return host.resolve("/" + endpointConfig).toURL();
   }

   protected static String getEndpointFromConfig(@Nullable final JsonObject source, final String id, final String def) {
      return source != null ? GsonHelper.getAsString(source, id, def) : def;
   }

   @Nullable
   public static ServerTextFilter createFromConfig(final DedicatedServerProperties config) {
      String textFilteringConfig = config.textFilteringConfig;
      if (StringUtil.isBlank(textFilteringConfig)) {
         return null;
      } else {
         return switch (config.textFilteringVersion) {
            case 0 -> LegacyTextFilter.createTextFilterFromConfig(textFilteringConfig);
            case 1 -> PlayerSafetyServiceTextFilter.createTextFilterFromConfig(textFilteringConfig);
            default -> {
               LOGGER.warn("Could not create text filter - unsupported text filtering version used");
               yield null;
            }
         };
      }
   }

   protected CompletableFuture<FilteredText> requestMessageProcessing(
      final GameProfile sender, final String message, final ServerTextFilter$IgnoreStrategy ignoreStrategy, final Executor executor
   ) {
      return message.isEmpty() ? CompletableFuture.completedFuture(FilteredText.EMPTY) : CompletableFuture.supplyAsync(() -> {
         JsonObject object = this.chatEncoder.encode(sender, message);

         try {
            JsonObject result = this.processRequestResponse(object, this.chatEndpoint);
            return this.filterText(message, ignoreStrategy, result);
         } catch (Exception var6) {
            LOGGER.warn("Failed to validate message '{}'", message, var6);
            return FilteredText.fullyFiltered(message);
         }
      }, executor);
   }

   protected abstract FilteredText filterText(final String message, final ServerTextFilter$IgnoreStrategy ignoreStrategy, final JsonObject result);

   protected FilterMask parseMask(final String message, final JsonArray removedChars, final ServerTextFilter$IgnoreStrategy ignoreStrategy) {
      if (removedChars.isEmpty()) {
         return FilterMask.PASS_THROUGH;
      } else if (ignoreStrategy.shouldIgnore(message, removedChars.size())) {
         return FilterMask.FULLY_FILTERED;
      } else {
         FilterMask mask = new FilterMask(message.length());

         for (int i = 0; i < removedChars.size(); i++) {
            mask.setFiltered(removedChars.get(i).getAsInt());
         }

         return mask;
      }
   }

   @Override
   public void close() {
      this.workerPool.shutdownNow();
   }

   protected void drainStream(final InputStream input) throws IOException {
      byte[] trashcan = new byte[1024];

      while (input.read(trashcan) != -1) {
      }
   }

   private JsonObject processRequestResponse(final JsonObject payload, final URL url) throws IOException {
      HttpURLConnection connection = this.makeRequest(payload, url);

      JsonObject var5;
      try (InputStream is = connection.getInputStream()) {
         if (connection.getResponseCode() == 204) {
            return new JsonObject();
         }

         try {
            var5 = LenientJsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8)).getAsJsonObject();
         } finally {
            this.drainStream(is);
         }
      }

      return var5;
   }

   protected HttpURLConnection makeRequest(final JsonObject payload, final URL url) throws IOException {
      HttpURLConnection connection = this.getURLConnection(url);
      this.setAuthorizationProperty(connection);
      OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);

      try {
         JsonWriter jsonWriter = new JsonWriter(writer);

         try {
            Streams.write(payload, jsonWriter);
         } catch (Throwable var10) {
            try {
               jsonWriter.close();
            } catch (Throwable var9) {
               var10.addSuppressed(var9);
            }

            throw var10;
         }

         jsonWriter.close();
      } catch (Throwable var11) {
         try {
            writer.close();
         } catch (Throwable var8) {
            var11.addSuppressed(var8);
         }

         throw var11;
      }

      writer.close();
      int responseCode = connection.getResponseCode();
      if (responseCode >= 200 && responseCode < 300) {
         return connection;
      } else {
         throw new ServerTextFilter$RequestFailedException(responseCode + " " + connection.getResponseMessage());
      }
   }

   protected abstract void setAuthorizationProperty(final HttpURLConnection connection);

   protected int connectionReadTimeout() {
      return 2000;
   }

   protected HttpURLConnection getURLConnection(final URL url) throws IOException {
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setConnectTimeout(15000);
      connection.setReadTimeout(this.connectionReadTimeout());
      connection.setUseCaches(false);
      connection.setDoOutput(true);
      connection.setDoInput(true);
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
      connection.setRequestProperty("Accept", "application/json");
      connection.setRequestProperty("User-Agent", "Minecraft server" + SharedConstants.getCurrentVersion().name());
      return connection;
   }

   public TextFilter createContext(final GameProfile gameProfile) {
      return new ServerTextFilter$PlayerContext(this, gameProfile);
   }
}
