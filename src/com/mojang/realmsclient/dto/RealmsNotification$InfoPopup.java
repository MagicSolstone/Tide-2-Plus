package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.Blaze3D;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.UUID;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PopupScreen;
import net.minecraft.client.gui.components.PopupScreen$Builder;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class RealmsNotification$InfoPopup extends RealmsNotification {
   private static final String TITLE = "title";
   private static final String MESSAGE = "message";
   private static final String IMAGE = "image";
   private static final String URL_BUTTON = "urlButton";
   private final RealmsText title;
   private final RealmsText message;
   private final Identifier image;
   @Nullable
   private final RealmsNotification$UrlButton urlButton;

   private RealmsNotification$InfoPopup(
      final RealmsNotification base,
      final RealmsText title,
      final RealmsText message,
      final Identifier image,
      @Nullable final RealmsNotification$UrlButton urlButton
   ) {
      super(base.uuid, base.dismissable, base.seen, base.type);
      this.title = title;
      this.message = message;
      this.image = image;
      this.urlButton = urlButton;
   }

   public static RealmsNotification$InfoPopup parse(final RealmsNotification base, final JsonObject object) {
      RealmsText title = JsonUtils.getRequired("title", object, RealmsText::parse);
      RealmsText message = JsonUtils.getRequired("message", object, RealmsText::parse);
      Identifier image = Identifier.parse(JsonUtils.getRequiredString("image", object));
      RealmsNotification$UrlButton urlButton = JsonUtils.getOptional("urlButton", object, RealmsNotification$UrlButton::parse);
      return new RealmsNotification$InfoPopup(base, title, message, image, urlButton);
   }

   @Nullable
   public PopupScreen buildScreen(final Screen parentScreen, final Consumer<UUID> dismiss) {
      Component title = this.title.createComponent();
      if (title == null) {
         RealmsNotification.LOGGER.warn("Realms info popup had title with no available translation: {}", this.title);
         return null;
      } else {
         PopupScreen$Builder builder = new PopupScreen$Builder(parentScreen, title)
            .setImage(this.image)
            .addMessage(this.message.createComponent(CommonComponents.EMPTY));
         if (this.urlButton != null) {
            builder.addButton(this.urlButton.urlText.createComponent(RealmsNotification.BUTTON_TEXT_FALLBACK), popup -> {
               Minecraft minecraft = Minecraft.getInstance();
               minecraft.gui.setScreen(new ConfirmLinkScreen(result -> {
                  if (result) {
                     Blaze3D.openUri(this.urlButton.url);
                     minecraft.gui.setScreen(parentScreen);
                  } else {
                     minecraft.gui.setScreen(popup);
                  }
               }, this.urlButton.url, true));
               dismiss.accept(this.uuid());
            });
         }

         builder.addButton(CommonComponents.GUI_OK, popup -> {
            popup.onClose();
            dismiss.accept(this.uuid());
         });
         builder.onClose(() -> dismiss.accept(this.uuid()));
         return builder.build();
      }
   }
}
