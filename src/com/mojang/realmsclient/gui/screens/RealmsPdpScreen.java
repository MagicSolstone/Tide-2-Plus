package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.gui.RealmsDataFetcher;
import com.mojang.realmsclient.gui.RealmsHeader;
import com.mojang.realmsclient.gui.task.DataFetcher$Subscription;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.RealmsButton;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonLinks;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class RealmsPdpScreen extends RealmsScreen {
   private static final Component TITLE = Component.translatable("mco.selectServer.purchase");
   private static final Component TRY_FOR_FREE = Component.translatable("mco.pdp.tryForFree");
   private static final Component TRIAL_TITLE = Component.translatable("mco.pdp.trial.title").withStyle(ChatFormatting.UNDERLINE);
   private static final Component TRIAL_DESCRIPTION = Component.translatable("mco.pdp.trial.description");
   private static final Component FRIENDS_TITLE = Component.translatable("mco.pdp.friends.title").withStyle(ChatFormatting.BOLD);
   private static final Component FRIENDS_DESCRIPTION = Component.translatable("mco.pdp.friends.description");
   private static final Component MINIGAMES_TITLE = Component.translatable("mco.pdp.minigames.title").withStyle(ChatFormatting.BOLD);
   private static final Component MINIGAMES_DESCRIPTION = Component.translatable("mco.pdp.minigames.description");
   private static final Component PRIVATE_SERVER_TITLE = Component.translatable("mco.pdp.private.title").withStyle(ChatFormatting.BOLD);
   private static final Component PRIVATE_SERVER_BACKUPS = Component.translatable("mco.pdp.private.backups");
   private static final Component PRIVATE_SERVER_EASY_TO_MANAGE = Component.translatable("mco.pdp.private.easyToManage");
   private static final Component PRIVATE_SERVER_WORLD_SLOTS = Component.translatable("mco.pdp.private.worldSlots");
   private static final Component PRIVATE_SERVER_SECURE = Component.translatable("mco.pdp.private.secure");
   private static final Identifier FRIENDS_IMAGE = Identifier.withDefaultNamespace("textures/gui/realms/friends.png");
   private static final Identifier MINIGAMES_IMAGE = Identifier.withDefaultNamespace("textures/gui/realms/minigames.png");
   private static final Identifier PRIVATE_SERVER_IMAGE = Identifier.withDefaultNamespace("textures/gui/realms/private_server.png");
   private static final Identifier TRIAL_AVAILABLE_SPRITE = Identifier.withDefaultNamespace("icon/trial_available");
   private static final int HEADER_HEIGHT = 44;
   private static final int ROW_WIDTH = 290;
   private static final int IMAGE_WIDTH = 80;
   private static final int IMAGE_HEIGHT = 45;
   private static final int SPACING = 16;
   private static final int TEXT_WIDTH = 182;
   private static final int HEADER_BUTTON_SPACING = 4;
   private final Screen lastScreen;
   private boolean trialAvailable;
   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
   private final RealmsHeader header;
   @Nullable
   private final Runnable openRealmsScreenWhenAvailable;
   private RealmsPdpScreen$PdpList pdpList;
   private DataFetcher$Subscription dataSubscription;
   private boolean realmsAvailable;
   private boolean redirectedToRealms;

   public RealmsPdpScreen(final Screen lastScreen, final boolean trialAvailable, final Runnable openJoinRealmScreen) {
      this(lastScreen, trialAvailable, openJoinRealmScreen, null);
   }

   public RealmsPdpScreen(
      final Screen lastScreen, final boolean trialAvailable, final Runnable openJoinRealmScreen, @Nullable final Runnable openRealmsScreenWhenAvailable
   ) {
      super(TITLE);
      this.lastScreen = lastScreen;
      this.trialAvailable = trialAvailable;
      this.openRealmsScreenWhenAvailable = openRealmsScreenWhenAvailable;
      this.header = new RealmsHeader(this, openJoinRealmScreen);
   }

   @Override
   public void init() {
      this.layout.removeChildren();
      this.layout.setHeaderHeight(44);
      this.layout.addToHeader(this.header.createLayout(realmsLogo(), 44, 4));
      this.dataSubscription = this.initDataFetcher(this.minecraft.realmsDataFetcher());
      this.pdpList = this.layout.addToContents(new RealmsPdpScreen$PdpList(this, this.minecraft));
      LinearLayout footer = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
      if (this.trialAvailable) {
         footer.addChild(new RealmsButton(0, 0, 150, 20, TRY_FOR_FREE, ConfirmLinkScreen.confirmLink(this, CommonLinks.START_REALMS_TRIAL)));
      } else {
         footer.addChild(Button.builder(TITLE, ConfirmLinkScreen.confirmLink(this, CommonLinks.BUY_REALMS)).build());
      }

      footer.addChild(Button.builder(CommonComponents.GUI_BACK, var1x -> this.onClose()).build());
      this.layout.visitWidgets(x$0 -> this.addRenderableWidget(x$0));
      this.repositionElements();
   }

   @Override
   public void tick() {
      super.tick();
      boolean previousTrialAvailable = this.trialAvailable;
      if (this.dataSubscription != null) {
         this.dataSubscription.tick();
      }

      if (previousTrialAvailable != this.trialAvailable && this.minecraft.gui.screen() == this) {
         this.rebuildWidgets();
      } else {
         if (!this.redirectedToRealms && this.realmsAvailable && this.openRealmsScreenWhenAvailable != null && this.minecraft.gui.screen() == this) {
            this.redirectedToRealms = true;
            this.openRealmsScreenWhenAvailable.run();
         }
      }
   }

   private DataFetcher$Subscription initDataFetcher(final RealmsDataFetcher dataSource) {
      DataFetcher$Subscription result = dataSource.dataFetcher.createSubscription();
      if (this.openRealmsScreenWhenAvailable != null) {
         result.subscribe(
            dataSource.serverListUpdateTask,
            serverListData -> this.realmsAvailable = !serverListData.serverList().isEmpty() || !serverListData.availableSnapshotServers().isEmpty()
         );
      }

      result.subscribe(dataSource.pendingInvitesTask, this.header::setPendingInvites);
      result.subscribe(dataSource.trialAvailabilityTask, trialAvailable -> this.trialAvailable = trialAvailable);
      result.subscribe(dataSource.newsTask, news -> {
         dataSource.newsManager.updateUnreadNews(news);
         this.header.setNews(dataSource.newsManager.newsLink(), dataSource.newsManager.hasUnreadNews());
      });
      return result;
   }

   @Override
   protected void repositionElements() {
      if (this.pdpList != null) {
         this.pdpList.updateSize(this.width, this.layout);
      }

      this.layout.arrangeElements();
   }

   @Override
   public void onClose() {
      this.minecraft.gui.setScreen(this.lastScreen);
   }
}
