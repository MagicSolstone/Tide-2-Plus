package com.mojang.realmsclient.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.util.LenientJsonParser;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class RealmsNotification {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final String NOTIFICATION_UUID = "notificationUuid";
   private static final String DISMISSABLE = "dismissable";
   private static final String SEEN = "seen";
   private static final String TYPE = "type";
   private static final String VISIT_URL = "visitUrl";
   private static final String INFO_POPUP = "infoPopup";
   private static final Component BUTTON_TEXT_FALLBACK = Component.translatable("mco.notification.visitUrl.buttonText.default");
   private final UUID uuid;
   private final boolean dismissable;
   private final boolean seen;
   private final String type;

   private RealmsNotification(final UUID uuid, final boolean dismissable, final boolean seen, final String type) {
      this.uuid = uuid;
      this.dismissable = dismissable;
      this.seen = seen;
      this.type = type;
   }

   public boolean seen() {
      return this.seen;
   }

   public boolean dismissable() {
      return this.dismissable;
   }

   public UUID uuid() {
      return this.uuid;
   }

   public static List<RealmsNotification> parseList(final String json) {
      List<RealmsNotification> result = new ArrayList<>();

      try {
         for (JsonElement element : LenientJsonParser.parse(json).getAsJsonObject().get("notifications").getAsJsonArray()) {
            result.add(parse(element.getAsJsonObject()));
         }
      } catch (Exception var5) {
         LOGGER.error("Could not parse list of RealmsNotifications", var5);
      }

      return result;
   }

   private static RealmsNotification parse(final JsonObject jsonObject) {
      UUID uuid = JsonUtils.getUuidOr("notificationUuid", jsonObject, null);
      if (uuid == null) {
         throw new IllegalStateException("Missing required property notificationUuid");
      } else {
         boolean dismissable = JsonUtils.getBooleanOr("dismissable", jsonObject, true);
         boolean seen = JsonUtils.getBooleanOr("seen", jsonObject, false);
         String type = JsonUtils.getRequiredString("type", jsonObject);
         RealmsNotification base = new RealmsNotification(uuid, dismissable, seen, type);

         return (RealmsNotification)(switch (type) {
            case "visitUrl" -> RealmsNotification$VisitUrl.parse(base, jsonObject);
            case "infoPopup" -> RealmsNotification$InfoPopup.parse(base, jsonObject);
            default -> base;
         });
      }
   }
}
