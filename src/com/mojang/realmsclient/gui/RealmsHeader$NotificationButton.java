package com.mojang.realmsclient.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button$OnPress;
import net.minecraft.client.gui.components.SpriteIconButton$CenteredIcon;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class RealmsHeader$NotificationButton extends SpriteIconButton$CenteredIcon {
   private static final Identifier[] NOTIFICATION_ICONS = new Identifier[]{
      Identifier.withDefaultNamespace("notification/1"),
      Identifier.withDefaultNamespace("notification/2"),
      Identifier.withDefaultNamespace("notification/3"),
      Identifier.withDefaultNamespace("notification/4"),
      Identifier.withDefaultNamespace("notification/5"),
      Identifier.withDefaultNamespace("notification/more")
   };
   private static final int UNKNOWN_COUNT = Integer.MAX_VALUE;
   private static final int SIZE = 20;
   private static final int SPRITE_SIZE = 14;
   private static final int NOTIFICATION_SIZE = 8;
   private static final int NOTIFICATION_X_OFFSET = -5;
   private static final int NOTIFICATION_Y_OFFSET = -3;
   private int notificationCount;

   public RealmsHeader$NotificationButton(final Component title, final Identifier texture, final Button$OnPress onPress, @Nullable final Component tooltip) {
      super(20, 20, title, 14, 14, 0, 0, new WidgetSprites(texture), onPress, tooltip, null, false);
   }

   private int notificationCount() {
      return this.notificationCount;
   }

   public void setNotificationCount(final int notificationCount) {
      this.notificationCount = notificationCount;
   }

   @Override
   public void extractContents(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      super.extractContents(graphics, mouseX, mouseY, a);
      if (this.isActive() && this.notificationCount != 0) {
         this.extractNotificationCounter(graphics);
      }
   }

   private void extractNotificationCounter(final GuiGraphicsExtractor graphics) {
      graphics.blitSprite(
         RenderPipelines.GUI_TEXTURED,
         NOTIFICATION_ICONS[Math.min(this.notificationCount, NOTIFICATION_ICONS.length) - 1],
         this.getX() + this.getWidth() + -5,
         this.getY() + -3,
         8,
         8
      );
   }
}
