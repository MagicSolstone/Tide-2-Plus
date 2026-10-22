package com.mojang.realmsclient.client;

import com.mojang.realmsclient.exception.RealmsHttpException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class Request$Put extends Request<Request$Put> {
   private final String content;

   public Request$Put(final String uri, final String content, final int connectTimeout, final int readTimeout) {
      super(uri, connectTimeout, readTimeout);
      this.content = content;
   }

   public Request$Put doConnect() {
      try {
         if (this.content != null) {
            this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
         }

         this.connection.setDoOutput(true);
         this.connection.setDoInput(true);
         this.connection.setRequestMethod("PUT");
         OutputStream out = this.connection.getOutputStream();
         OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
         writer.write(this.content);
         writer.close();
         out.flush();
         return this;
      } catch (Exception var3) {
         throw new RealmsHttpException(var3.getMessage(), var3);
      }
   }
}
