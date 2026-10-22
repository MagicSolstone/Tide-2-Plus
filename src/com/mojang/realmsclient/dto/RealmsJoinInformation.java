package com.mojang.realmsclient.dto;

import com.google.gson.annotations.SerializedName;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public record RealmsJoinInformation(
   @Nullable @SerializedName("address") String address,
   @Nullable @SerializedName("resourcePackUrl") String resourcePackUrl,
   @Nullable @SerializedName("resourcePackHash") String resourcePackHash,
   @Nullable @SerializedName("sessionRegionData") RealmsJoinInformation$RegionData regionData
) implements ReflectionBasedSerialization {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final RealmsJoinInformation EMPTY = new RealmsJoinInformation(null, null, null, null);

   public static RealmsJoinInformation parse(final GuardedSerializer gson, final String json) {
      try {
         RealmsJoinInformation server = gson.fromJson(json, RealmsJoinInformation.class);
         if (server == null) {
            LOGGER.error("Could not parse RealmsServerAddress: {}", json);
            return EMPTY;
         } else {
            return server;
         }
      } catch (Exception var3) {
         LOGGER.error("Could not parse RealmsServerAddress", var3);
         return EMPTY;
      }
   }
}
