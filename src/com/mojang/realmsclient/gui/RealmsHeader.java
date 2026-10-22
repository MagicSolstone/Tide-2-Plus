package com.mojang.realmsclient.gui;

import com.mojang.realmsclient.gui.screens.RealmsPendingInvitesScreen;
import com.mojang.realmsclient.util.RealmsPersistence;
import com.mojang.realmsclient.util.RealmsPersistence$RealmsPersistenceData;
import java.net.URI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class RealmsHeader {
   private static final Identifier INVITE_SPRITE = Identifier.withDefaultNamespace("icon/invite");
   private static final Identifier NEWS_SPRITE = Identifier.withDefaultNamespace("icon/news");
   private static final int CONTENT_WIDTH = 308;
   private static final Tooltip NO_PENDING_INVITES_TOOLTIP = Tooltip.create(Component.translatable("mco.invites.nopending"));
   private static final Tooltip PENDING_INVITES_TOOLTIP = Tooltip.create(Component.translatable("mco.invites.pending"));
   private static final Component INVITES_TITLE = Component.translatable("mco.invites.title");
   private static final Component NEWS_TITLE = Component.translatable("mco.news");
   private static final Component JOIN_REALM_TEXT = Component.translatable("mco.selectServer.joinRealm");
   private final Minecraft minecraft = Minecraft.getInstance();
   private final Screen screen;
   private final Button joinRealmButton;
   private final RealmsHeader$NotificationButton pendingInvitesButton;
   private final RealmsHeader$NotificationButton newsButton;
   @Nullable
   private volatile URI newsLink;

   public RealmsHeader(final Screen screen, final Runnable openJoinRealmScreen) {
      this.screen = screen;
      this.pendingInvitesButton = new RealmsHeader$NotificationButton(
         INVITES_TITLE, INVITE_SPRITE, var2 -> this.minecraft.gui.setScreen(new RealmsPendingInvitesScreen(screen, INVITES_TITLE)), null
      );
      this.newsButton = new RealmsHeader$NotificationButton(NEWS_TITLE, NEWS_SPRITE, var1 -> this.openNews(), NEWS_TITLE);
      this.joinRealmButton = Button.builder(JOIN_REALM_TEXT, button -> openJoinRealmScreen.run()).build();
   }

   public Layout createLayout(final ImageWidget logo, final int headerHeight, final int buttonSpacing) {
      int sideCellWidth = (308 - logo.getWidth()) / 2;
      this.joinRealmButton.setWidth(Math.min(this.minecraft.font.width(JOIN_REALM_TEXT) + 16, sideCellWidth));
      LinearLayout buttons = LinearLayout.horizontal().spacing(buttonSpacing);
      buttons.defaultCellSetting().alignVerticallyMiddle();
      buttons.addChild(this.pendingInvitesButton);
      buttons.addChild(this.newsButton);
      LinearLayout centeredContent = LinearLayout.horizontal();
      centeredContent.defaultCellSetting().alignVerticallyMiddle();
      centeredContent.addChild(new FrameLayout(sideCellWidth, headerHeight)).addChild(this.joinRealmButton, LayoutSettings::alignHorizontallyLeft);
      centeredContent.addChild(logo, LayoutSettings::alignHorizontallyCenter);
      centeredContent.addChild(new FrameLayout(sideCellWidth, headerHeight)).addChild(buttons, LayoutSettings::alignHorizontallyRight);
      return centeredContent;
   }

   public void setPendingInvites(final int numberOfPendingInvites) {
      this.pendingInvitesButton.setNotificationCount(numberOfPendingInvites);
      this.pendingInvitesButton.setTooltip(numberOfPendingInvites == 0 ? NO_PENDING_INVITES_TOOLTIP : PENDING_INVITES_TOOLTIP);
   }

   public void setNews(@Nullable final URI newsLink, final boolean hasUnreadNews) {
      this.newsLink = newsLink;
      this.newsButton.setNotificationCount(hasUnreadNews ? Integer.MAX_VALUE : 0);
   }

   public void setJoinRealmButtonActive(final boolean active) {
      this.joinRealmButton.active = active;
   }

   private void openNews() {
      URI newsLink = this.newsLink;
      if (newsLink != null) {
         ConfirmLinkScreen.confirmLinkNow(this.screen, newsLink);
         if (this.newsButton.notificationCount() != 0) {
            RealmsPersistence$RealmsPersistenceData data = RealmsPersistence.readFile();
            data.hasUnreadNews = false;
            RealmsPersistence.writeFile(data);
            this.newsButton.setNotificationCount(0);
         }
      }
   }
}
