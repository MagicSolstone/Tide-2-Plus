package com.mojang.realmsclient.client;

import com.mojang.realmsclient.exception.RealmsHttpException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class Request$Delete extends Request<Request$Delete> {
   public Request$Delete(final String uri, final int connectTimeout, final int readTimeout) {
      super(uri, connectTimeout, readTimeout);
   }

   public Request$Delete doConnect() {
      try {
         this.connection.setDoOutput(true);
         this.connection.setRequestMethod("DELETE");
         this.connection.connect();
         return this;
      } catch (Exception var2) {
         throw new RealmsHttpException(var2.getMessage(), var2);
      }
   }
}
