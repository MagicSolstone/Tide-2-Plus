package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import java.net.URI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class RealmsNotification$VisitUrl extends RealmsNotification {
   private static final String URL = "url";
   private static final String BUTTON_TEXT = "buttonText";
   private static final String MESSAGE = "message";
   private final URI url;
   private final RealmsText buttonText;
   private final RealmsText message;

   private RealmsNotification$VisitUrl(final RealmsNotification base, final URI url, final RealmsText buttonText, final RealmsText message) {
      super(base.uuid, base.dismissable, base.seen, base.type);
      this.url = url;
      this.buttonText = buttonText;
      this.message = message;
   }

   public static RealmsNotification$VisitUrl parse(final RealmsNotification base, final JsonObject jsonObject) {
      URI url = JsonUtils.getRequiredUri("url", jsonObject);
      RealmsText buttonText = JsonUtils.getRequired("buttonText", jsonObject, RealmsText::parse);
      RealmsText message = JsonUtils.getRequired("message", jsonObject, RealmsText::parse);
      return new RealmsNotification$VisitUrl(base, url, buttonText, message);
   }

   public Component getMessage() {
      return this.message.createComponent(Component.translatable("mco.notification.visitUrl.message.default"));
   }

   public Button buildOpenLinkButton(final Screen parentScreen) {
      Component buttonLabel = this.buttonText.createComponent(RealmsNotification.BUTTON_TEXT_FALLBACK);
      return Button.builder(buttonLabel, ConfirmLinkScreen.confirmLink(parentScreen, this.url)).build();
   }
}
