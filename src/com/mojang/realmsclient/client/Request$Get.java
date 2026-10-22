package com.mojang.realmsclient.client;

import com.mojang.realmsclient.exception.RealmsHttpException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class Request$Get extends Request<Request$Get> {
   public Request$Get(final String uri, final int connectTimeout, final int readTimeout) {
      super(uri, connectTimeout, readTimeout);
   }

   public Request$Get doConnect() {
      try {
         this.connection.setDoInput(true);
         this.connection.setDoOutput(true);
         this.connection.setUseCaches(false);
         this.connection.setRequestMethod("GET");
         return this;
      } catch (Exception var2) {
         throw new RealmsHttpException(var2.getMessage(), var2);
      }
   }
}
