package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import java.net.URI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record RealmsNotification$UrlButton(URI url, RealmsText urlText) {
   private static final String URL = "url";
   private static final String URL_TEXT = "urlText";

   public static RealmsNotification$UrlButton parse(final JsonObject jsonObject) {
      URI url = URI.create(JsonUtils.getRequiredString("url", jsonObject));
      RealmsText urlText = JsonUtils.getRequired("urlText", jsonObject, RealmsText::parse);
      return new RealmsNotification$UrlButton(url, urlText);
   }
}
