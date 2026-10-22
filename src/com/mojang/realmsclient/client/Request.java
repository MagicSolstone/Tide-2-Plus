package com.mojang.realmsclient.client;

import com.mojang.realmsclient.exception.RealmsHttpException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class Request<T extends Request<T>> {
   protected HttpURLConnection connection;
   private boolean connected;
   protected String url;
   private static final int DEFAULT_READ_TIMEOUT = 60000;
   private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
   private static final String NETWORK_PROTOCOL_KEY = "X-NetworkProtocolVersion";
   private static final String CLIENT_REF_KEY = "Client-Ref";
   private static final String IS_SNAPSHOT_KEY = "Is-Prerelease";
   private static final String COOKIE_KEY = "Cookie";

   public Request(final String url, final int connectTimeout, final int readTimeout) {
      try {
         this.url = url;
         Proxy proxy = RealmsClientConfig.getProxy();
         if (proxy != null) {
            this.connection = (HttpURLConnection)new URL(url).openConnection(proxy);
         } else {
            this.connection = (HttpURLConnection)new URL(url).openConnection();
         }

         this.connection.setConnectTimeout(connectTimeout);
         this.connection.setReadTimeout(readTimeout);
      } catch (MalformedURLException var5) {
         throw new RealmsHttpException(var5.getMessage(), var5);
      } catch (IOException var6) {
         throw new RealmsHttpException(var6.getMessage(), var6);
      }
   }

   public void cookie(final String key, final String value) {
      cookie(this.connection, key, value);
   }

   public static void cookie(final HttpURLConnection connection, final String key, final String value) {
      String cookie = connection.getRequestProperty("Cookie");
      if (cookie == null) {
         connection.setRequestProperty("Cookie", key + "=" + value);
      } else {
         connection.setRequestProperty("Cookie", cookie + ";" + key + "=" + value);
      }
   }

   public void addVersionHeaders(final String id, final int networkProtocol, final boolean isSnapshot) {
      this.connection.addRequestProperty("Client-Ref", id);
      this.connection.addRequestProperty("X-NetworkProtocolVersion", String.valueOf(networkProtocol));
      this.connection.addRequestProperty("Is-Prerelease", String.valueOf(isSnapshot));
   }

   public int getRetryAfterHeader() {
      return getRetryAfterHeader(this.connection);
   }

   public static int getRetryAfterHeader(final HttpURLConnection connection) {
      String pauseTime = connection.getHeaderField("Retry-After");

      try {
         return Integer.valueOf(pauseTime);
      } catch (Exception var3) {
         return 5;
      }
   }

   public int responseCode() {
      try {
         this.connect();
         return this.connection.getResponseCode();
      } catch (Exception var2) {
         throw new RealmsHttpException(var2.getMessage(), var2);
      }
   }

   public String text() {
      try {
         this.connect();
         String result;
         if (this.responseCode() >= 400) {
            result = this.read(this.connection.getErrorStream());
         } else {
            result = this.read(this.connection.getInputStream());
         }

         this.dispose();
         return result;
      } catch (IOException var2) {
         throw new RealmsHttpException(var2.getMessage(), var2);
      }
   }

   private String read(@Nullable final InputStream in) throws IOException {
      if (in == null) {
         return "";
      } else {
         InputStreamReader streamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
         StringBuilder sb = new StringBuilder();

         for (int x = streamReader.read(); x != -1; x = streamReader.read()) {
            sb.append((char)x);
         }

         return sb.toString();
      }
   }

   private void dispose() {
      byte[] bytes = new byte[1024];

      try {
         InputStream in = this.connection.getInputStream();

         while (in.read(bytes) > 0) {
         }

         in.close();
         return;
      } catch (Exception var9) {
         try {
            InputStream errorStream = this.connection.getErrorStream();
            if (errorStream != null) {
               while (errorStream.read(bytes) > 0) {
               }

               errorStream.close();
               return;
            }
         } catch (IOException var8) {
            return;
         }
      } finally {
         if (this.connection != null) {
            this.connection.disconnect();
         }
      }
   }

   protected T connect() {
      if (this.connected) {
         return (T)this;
      } else {
         T t = this.doConnect();
         this.connected = true;
         return t;
      }
   }

   protected abstract T doConnect();

   public static Request<?> get(final String url) {
      return new Request$Get(url, 5000, 60000);
   }

   public static Request<?> get(final String url, final int connectTimeoutMillis, final int readTimeoutMillis) {
      return new Request$Get(url, connectTimeoutMillis, readTimeoutMillis);
   }

   public static Request<?> post(final String uri, final String content) {
      return new Request$Post(uri, content, 5000, 60000);
   }

   public static Request<?> post(final String uri, final String content, final int connectTimeoutMillis, final int readTimeoutMillis) {
      return new Request$Post(uri, content, connectTimeoutMillis, readTimeoutMillis);
   }

   public static Request<?> delete(final String url) {
      return new Request$Delete(url, 5000, 60000);
   }

   public static Request<?> put(final String url, final String content) {
      return new Request$Put(url, content, 5000, 60000);
   }

   public static Request<?> put(final String url, final String content, final int connectTimeoutMillis, final int readTimeoutMillis) {
      return new Request$Put(url, content, connectTimeoutMillis, readTimeoutMillis);
   }

   public String getHeader(final String header) {
      return getHeader(this.connection, header);
   }

   public static String getHeader(final HttpURLConnection connection, final String header) {
      try {
         return connection.getHeaderField(header);
      } catch (Exception var3) {
         return "";
      }
   }
}
