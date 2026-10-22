package com.mojang.realmsclient;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.client.RealmsClient$CompatibleVersionResponse;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class RealmsAvailability {
   private static final Logger LOGGER = LogUtils.getLogger();
   @Nullable
   private static CompletableFuture<RealmsAvailability$Result> future;

   public static CompletableFuture<RealmsAvailability$Result> get() {
      if (future == null || shouldRefresh(future)) {
         future = check();
      }

      return future;
   }

   private static boolean shouldRefresh(final CompletableFuture<RealmsAvailability$Result> future) {
      RealmsAvailability$Result result = future.getNow(null);
      return result != null && result.exception() != null;
   }

   private static CompletableFuture<RealmsAvailability$Result> check() {
      if (Minecraft.getInstance().isOfflineDeveloperMode()) {
         return CompletableFuture.completedFuture(new RealmsAvailability$Result(RealmsAvailability$Type.AUTHENTICATION_ERROR));
      } else {
         return SharedConstants.DEBUG_BYPASS_REALMS_VERSION_CHECK
            ? CompletableFuture.completedFuture(new RealmsAvailability$Result(RealmsAvailability$Type.SUCCESS))
            : CompletableFuture.supplyAsync(
               () -> {
                  RealmsClient client = RealmsClient.getOrCreate();

                  try {
                     if (client.clientCompatible() != RealmsClient$CompatibleVersionResponse.COMPATIBLE) {
                        return new RealmsAvailability$Result(RealmsAvailability$Type.INCOMPATIBLE_CLIENT);
                     } else {
                        return !client.hasParentalConsent()
                           ? new RealmsAvailability$Result(RealmsAvailability$Type.NEEDS_PARENTAL_CONSENT)
                           : new RealmsAvailability$Result(RealmsAvailability$Type.SUCCESS);
                     }
                  } catch (RealmsServiceException var2) {
                     LOGGER.error("Couldn't connect to realms", var2);
                     return var2.realmsError.errorCode() == 401
                        ? new RealmsAvailability$Result(RealmsAvailability$Type.AUTHENTICATION_ERROR)
                        : new RealmsAvailability$Result(var2);
                  }
               },
               Util.nonCriticalIoPool()
            );
      }
   }
}
