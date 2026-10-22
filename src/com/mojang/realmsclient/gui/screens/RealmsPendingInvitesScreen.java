package com.mojang.realmsclient.gui.screens;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.PendingInvite;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.util.Util;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class RealmsPendingInvitesScreen extends RealmsScreen {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Component NO_PENDING_INVITES_TEXT = Component.translatable("mco.invites.nopending");
   private final Screen lastScreen;
   private final CompletableFuture<List<PendingInvite>> pendingInvites = CompletableFuture.supplyAsync(() -> {
      try {
         return RealmsClient.getOrCreate().pendingInvites().pendingInvites();
      } catch (RealmsServiceException var1) {
         LOGGER.error("Couldn't list invites", var1);
         return List.of();
      }
   }, Util.nonCriticalIoPool());
   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
   private RealmsPendingInvitesScreen$PendingInvitationSelectionList pendingInvitationSelectionList;

   public RealmsPendingInvitesScreen(final Screen lastScreen, final Component title) {
      super(title);
      this.lastScreen = lastScreen;
   }

   @Override
   public void init() {
      RealmsMainScreen.refreshPendingInvites();
      this.layout.addTitleHeader(this.title, this.font);
      this.pendingInvitationSelectionList = this.layout.addToContents(new RealmsPendingInvitesScreen$PendingInvitationSelectionList(this, this.minecraft));
      this.pendingInvites.thenAcceptAsync(invites -> {
         List<RealmsPendingInvitesScreen$Entry> entries = invites.stream().map(x$0 -> new RealmsPendingInvitesScreen$Entry(this, x$0)).toList();
         this.pendingInvitationSelectionList.replaceEntries(entries);
         if (entries.isEmpty()) {
            this.minecraft.getNarrator().saySystemQueued(NO_PENDING_INVITES_TEXT);
         }
      }, this.screenExecutor);
      this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(200).build());
      this.layout.visitWidgets(x$0 -> this.addRenderableWidget(x$0));
      this.repositionElements();
   }

   @Override
   protected void repositionElements() {
      this.layout.arrangeElements();
      if (this.pendingInvitationSelectionList != null) {
         this.pendingInvitationSelectionList.updateSize(this.width, this.layout);
      }
   }

   @Override
   public void onClose() {
      this.minecraft.gui.setScreen(this.lastScreen);
   }

   @Override
   public void extractRenderState(final GuiGraphicsExtractor graphics, final int xm, final int ym, final float a) {
      super.extractRenderState(graphics, xm, ym, a);
      if (this.pendingInvites.isDone() && this.pendingInvitationSelectionList.hasPendingInvites()) {
         graphics.centeredText(this.font, NO_PENDING_INVITES_TEXT, this.width / 2, this.height / 2 - 20, -1);
      }
   }
}
