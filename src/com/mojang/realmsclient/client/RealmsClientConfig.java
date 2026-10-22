package com.mojang.realmsclient.client;

import java.net.Proxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class RealmsClientConfig {
   @Nullable
   private static Proxy proxy;

   @Nullable
   public static Proxy getProxy() {
      return proxy;
   }

   public static void setProxy(final Proxy proxy) {
      if (RealmsClientConfig.proxy == null) {
         RealmsClientConfig.proxy = proxy;
      }
   }
}
