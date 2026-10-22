package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.util.JsonUtils;
import java.net.URI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.LenientJsonParser;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public record RealmsNews(@Nullable URI newsLink) {
   private static final Logger LOGGER = LogUtils.getLogger();

   public static RealmsNews parse(final String json) {
      URI newsLink = null;

      try {
         JsonObject object = LenientJsonParser.parse(json).getAsJsonObject();
         newsLink = JsonUtils.getUriNullable("newsLink", object);
      } catch (Exception var3) {
         LOGGER.error("Could not parse RealmsNews", var3);
      }

      return new RealmsNews(newsLink);
   }
}
