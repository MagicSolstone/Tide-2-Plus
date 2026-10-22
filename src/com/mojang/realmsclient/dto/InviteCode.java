package com.mojang.realmsclient.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.util.JsonUtils;
import java.time.Instant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.LenientJsonParser;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public record InviteCode(String code, @Nullable Instant expirationDate, boolean enabled) {
   private static final Logger LOGGER = LogUtils.getLogger();

   @Nullable
   public static InviteCode parse(final String json) {
      try {
         JsonObject object = LenientJsonParser.parse(json).getAsJsonObject();
         JsonElement result = object.get("result");
         JsonObject payload = result != null && result.isJsonObject() ? result.getAsJsonObject() : object;
         return parse(payload);
      } catch (Exception var4) {
         LOGGER.error("Could not parse InviteCode", var4);
         return null;
      }
   }

   @Nullable
   public static InviteCode parse(final JsonObject json) {
      try {
         String code = JsonUtils.getStringOr("code", json, "");
         if (code.isEmpty()) {
            LOGGER.error("Invite code response was missing a code");
            return null;
         } else {
            return new InviteCode(code, JsonUtils.getEpochMillisOr("expirationDate", json, null), JsonUtils.getBooleanOr("enabled", json, false));
         }
      } catch (Exception var2) {
         LOGGER.error("Could not parse InviteCode", var2);
         return null;
      }
   }
}
