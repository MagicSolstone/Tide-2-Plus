package com.mojang.realmsclient.dto;

import com.google.gson.annotations.SerializedName;
import com.mojang.logging.LogUtils;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public record RealmsServerList(@SerializedName("servers") List<RealmsServer> servers) implements ReflectionBasedSerialization {
   private static final Logger LOGGER = LogUtils.getLogger();

   public static RealmsServerList parse(final GuardedSerializer gson, final String json) {
      try {
         RealmsServerList realmsServerList = gson.fromJson(json, RealmsServerList.class);
         if (realmsServerList != null) {
            realmsServerList.servers.forEach(RealmsServer::finalize);
            return realmsServerList;
         }

         LOGGER.error("Could not parse McoServerList: {}", json);
      } catch (Exception var3) {
         LOGGER.error("Could not parse McoServerList", var3);
      }

      return new RealmsServerList(List.of());
   }
}
