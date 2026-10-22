package com.mojang.realmsclient;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.RateLimiter;
import com.mojang.blaze3d.Blaze3D;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.Ping;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.client.RealmsClient$Environment;
import com.mojang.realmsclient.dto.PingResult;
import com.mojang.realmsclient.dto.RealmsNotification;
import com.mojang.realmsclient.dto.RealmsNotification$InfoPopup;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsServer$State;
import com.mojang.realmsclient.dto.RealmsServerPlayerLists;
import com.mojang.realmsclient.dto.RegionPingResult;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.RealmsDataFetcher;
import com.mojang.realmsclient.gui.RealmsHeader;
import com.mojang.realmsclient.gui.RealmsServerList;
import com.mojang.realmsclient.gui.screens.RealmsJoinRealmWithCodeScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.gui.screens.RealmsPdpScreen;
import com.mojang.realmsclient.gui.screens.RealmsPopups;
import com.mojang.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.gui.task.DataFetcher$Subscription;
import com.mojang.realmsclient.gui.task.DataFetcher$Task;
import com.mojang.realmsclient.util.task.GetServerDetailsTask;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.client.gui.components.FocusableTextWidget$BackgroundFill;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.LoadingDotsWidget;
import net.minecraft.client.gui.components.PopupScreen;
import net.minecraft.client.gui.components.PopupScreen$Builder;
import net.minecraft.client.gui.components.RealmsButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.GridLayout$RowHelper;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonLinks;
import net.minecraft.util.CommonLinks$ExtensionReference;
import net.minecraft.util.Util;
import net.minecraft.world.level.GameType;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class RealmsMainScreen extends RealmsScreen {
   private static final Identifier INFO_SPRITE = Identifier.withDefaultNamespace("icon/info");
   private static final Identifier NEW_REALM_SPRITE = Identifier.withDefaultNamespace("icon/new_realm");
   private static final Identifier EXPIRED_SPRITE = Identifier.withDefaultNamespace("realm_status/expired");
   private static final Identifier EXPIRES_SOON_SPRITE = Identifier.withDefaultNamespace("realm_status/expires_soon");
   private static final Identifier OPEN_SPRITE = Identifier.withDefaultNamespace("realm_status/open");
   private static final Identifier CLOSED_SPRITE = Identifier.withDefaultNamespace("realm_status/closed");
   public static final Identifier HARDCORE_MODE_SPRITE = Identifier.withDefaultNamespace("hud/heart/hardcore_full");
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Identifier NO_REALMS_LOCATION = Identifier.withDefaultNamespace("textures/gui/realms/no_realms.png");
   private static final Component TITLE = Component.translatable("menu.online");
   private static final Component LOADING_TEXT = Component.translatable("mco.selectServer.loading");
   private static final Component SERVER_UNITIALIZED_TEXT = Component.translatable("mco.selectServer.uninitialized");
   private static final Component SUBSCRIPTION_EXPIRED_TEXT = Component.translatable("mco.selectServer.expiredList");
   private static final Component SUBSCRIPTION_RENEW_TEXT = Component.translatable("mco.selectServer.expiredRenew");
   private static final Component TRIAL_EXPIRED_TEXT = Component.translatable("mco.selectServer.expiredTrial");
   private static final Component PLAY_TEXT = Component.translatable("mco.selectServer.play");
   private static final Component LEAVE_SERVER_TEXT = Component.translatable("mco.selectServer.leave");
   private static final Component CONFIGURE_SERVER_TEXT = Component.translatable("mco.selectServer.configure");
   private static final Component SERVER_EXPIRED_TOOLTIP = Component.translatable("mco.selectServer.expired");
   private static final Component SERVER_EXPIRES_SOON_TOOLTIP = Component.translatable("mco.selectServer.expires.soon");
   private static final Component SERVER_EXPIRES_IN_DAY_TOOLTIP = Component.translatable("mco.selectServer.expires.day");
   private static final Component SERVER_OPEN_TOOLTIP = Component.translatable("mco.selectServer.open");
   private static final Component SERVER_CLOSED_TOOLTIP = Component.translatable("mco.selectServer.closed");
   private static final Component UNITIALIZED_WORLD_NARRATION = Component.translatable("gui.narrate.button", SERVER_UNITIALIZED_TEXT);
   private static final Component NO_REALMS_TEXT = Component.translatable("mco.selectServer.noRealms");
   private static final Component ADD_REALM_TEXT = Component.translatable("mco.selectServer.purchase");
   private static final Component FREE_TRIAL = Component.translatable("mco.selectServer.freeTrial");
   private static final Component INCOMPATIBLE_POPUP_TITLE = Component.translatable("mco.compatibility.incompatible.popup.title");
   private static final Component INCOMPATIBLE_RELEASE_TYPE_POPUP_MESSAGE = Component.translatable("mco.compatibility.incompatible.releaseType.popup.message");
   private static final int BUTTON_WIDTH = 100;
   private static final int BUTTON_COLUMNS = 3;
   private static final int BUTTON_SPACING = 4;
   private static final int CONTENT_WIDTH = 308;
   private static final int LOGO_PADDING = 5;
   private static final int HEADER_HEIGHT = 44;
   private static final int FOOTER_PADDING = 11;
   private static final int NEW_REALM_SPRITE_WIDTH = 40;
   private static final int NEW_REALM_SPRITE_HEIGHT = 20;
   private static final boolean SNAPSHOT = !SharedConstants.getCurrentVersion().stable();
   private static boolean snapshotToggle = SNAPSHOT;
   private final CompletableFuture<RealmsAvailability$Result> availability = RealmsAvailability.get();
   private DataFetcher$Subscription dataSubscription;
   private final Set<UUID> handledSeenNotifications = new HashSet<>();
   private static boolean regionsPinged;
   private final RateLimiter inviteNarrationLimiter;
   private final Screen lastScreen;
   private Button playButton;
   private Button backButton;
   private Button renewButton;
   private Button configureButton;
   private Button leaveButton;
   private RealmsMainScreen$RealmSelectionList realmSelectionList;
   private RealmsServerList serverList;
   private List<RealmsServer> availableSnapshotServers = List.of();
   private RealmsServerPlayerLists onlinePlayersPerRealm = new RealmsServerPlayerLists(Map.of());
   private boolean redirectToPdpIfNoRealms;
   private boolean trialAvailable;
   private final List<RealmsNotification> notifications = new ArrayList<>();
   private final RealmsHeader header;
   private Button addRealmButton;
   private RealmsMainScreen$LayoutState activeLayoutState;
   @Nullable
   private HeaderAndFooterLayout layout;

   public RealmsMainScreen(final Screen lastScreen) {
      super(TITLE);
      this.lastScreen = lastScreen;
      this.redirectToPdpIfNoRealms = lastScreen instanceof TitleScreen && Minecraft.getInstance().gui.screen() == lastScreen;
      this.header = new RealmsHeader(this, this::openJoinRealmScreen);
      this.inviteNarrationLimiter = RateLimiter.create(0.016666668F);
   }

   @Override
   public void init() {
      this.serverList = new RealmsServerList(this.minecraft);
      this.realmSelectionList = new RealmsMainScreen$RealmSelectionList(this);
      this.playButton = Button.builder(PLAY_TEXT, button -> play(this.getSelectedServer(), this)).width(100).build();
      this.configureButton = Button.builder(CONFIGURE_SERVER_TEXT, button -> this.configureClicked(this.getSelectedServer())).width(100).build();
      this.renewButton = Button.builder(SUBSCRIPTION_RENEW_TEXT, button -> this.onRenew(this.getSelectedServer())).width(100).build();
      this.leaveButton = Button.builder(LEAVE_SERVER_TEXT, button -> this.leaveClicked(this.getSelectedServer())).width(100).build();
      this.addRealmButton = this.createAddRealmButton();
      this.backButton = Button.builder(CommonComponents.GUI_BACK, button -> this.onClose()).width(100).build();
      this.updateLayout(RealmsMainScreen$LayoutState.LOADING);
      this.updateButtonStates();
      this.availability.thenAcceptAsync(result -> {
         Screen errorScreen = result.createErrorScreen(this.lastScreen);
         if (errorScreen == null) {
            this.dataSubscription = this.initDataFetcher(this.minecraft.realmsDataFetcher());
         } else {
            this.minecraft.gui.setScreen(errorScreen);
         }
      }, this.screenExecutor);
   }

   public static boolean isSnapshot() {
      return SNAPSHOT && snapshotToggle;
   }

   @Override
   protected void repositionElements() {
      if (this.layout != null) {
         this.realmSelectionList.updateSize(this.width, this.layout);
         this.layout.arrangeElements();
      }
   }

   @Override
   public void onClose() {
      this.minecraft.gui.setScreen(this.lastScreen);
   }

   private void updateLayout() {
      if (this.serverList.isEmpty() && this.availableSnapshotServers.isEmpty() && this.notifications.isEmpty()) {
         this.updateLayout(RealmsMainScreen$LayoutState.NO_REALMS);
      } else {
         this.updateLayout(RealmsMainScreen$LayoutState.LIST);
      }
   }

   private void updateLayout(final RealmsMainScreen$LayoutState state) {
      this.updateLayout(state, false);
   }

   private void updateLayout(final RealmsMainScreen$LayoutState state, final boolean force) {
      if (force || this.activeLayoutState != state) {
         if (this.layout != null) {
            this.layout.visitWidgets(x$0 -> this.removeWidget(x$0));
         }

         this.layout = this.createLayout(state);
         this.activeLayoutState = state;
         this.layout.visitWidgets(x$0 -> this.addRenderableWidget(x$0));
         this.repositionElements();
      }
   }

   private HeaderAndFooterLayout createLayout(final RealmsMainScreen$LayoutState state) {
      HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
      layout.setHeaderHeight(44);
      layout.addToHeader(this.createHeader());
      Layout footer = this.createFooter(state);
      footer.arrangeElements();
      layout.setFooterHeight(footer.getHeight() + 22);
      layout.addToFooter(footer);
      switch (state) {
         case LOADING:
            layout.addToContents(new LoadingDotsWidget(this.font, LOADING_TEXT));
            break;
         case NO_REALMS:
            layout.addToContents(this.createNoRealmsContent());
            break;
         case LIST:
            layout.addToContents(this.realmSelectionList);
      }

      return layout;
   }

   private Layout createHeader() {
      return this.header.createLayout(realmsLogo(), 44, 4);
   }

   private Button createAddRealmButton() {
      return (Button)(this.trialAvailable
         ? new RealmsButton(0, 0, 100, 20, FREE_TRIAL, var1 -> this.openRealmsPdp())
         : Button.builder(ADD_REALM_TEXT, var1 -> this.openRealmsPdp()).size(100, 20).build());
   }

   private Layout createFooter(final RealmsMainScreen$LayoutState state) {
      GridLayout footer = new GridLayout().spacing(4);
      GridLayout$RowHelper helper = footer.createRowHelper(3);
      if (RealmsClient.ENVIRONMENT == RealmsClient$Environment.STAGE) {
         helper.addChild(
            CycleButton.booleanBuilder(Component.literal("Snapshot"), Component.literal("Release"), snapshotToggle)
               .create(0, 0, 100, 20, Component.literal("Realm"), (button, value) -> {
                  snapshotToggle = value;
                  this.availableSnapshotServers = List.of();
                  this.debugRefreshDataFetchers();
               }),
            3,
            helper.newCellSettings().alignHorizontallyCenter()
         );
      }

      if (state == RealmsMainScreen$LayoutState.LIST) {
         helper.addChild(this.playButton);
         helper.addChild(this.configureButton);
         helper.addChild(this.renewButton);
         helper.addChild(this.leaveButton);
      }

      helper.addChild(this.addRealmButton);
      helper.addChild(this.backButton);
      return footer;
   }

   private LinearLayout createNoRealmsContent() {
      LinearLayout content = LinearLayout.vertical().spacing(8);
      content.defaultCellSetting().alignHorizontallyCenter();
      content.addChild(ImageWidget.texture(130, 64, NO_REALMS_LOCATION, 130, 64));
      content.addChild(
         FocusableTextWidget.builder(NO_REALMS_TEXT, this.font)
            .maxWidth(308)
            .alwaysShowBorder(false)
            .backgroundFill(FocusableTextWidget$BackgroundFill.ON_FOCUS)
            .build()
      );
      return content;
   }

   private void updateButtonStates() {
      RealmsServer server = this.getSelectedServer();
      boolean serverSelected = server != null;
      this.addRealmButton.active = this.activeLayoutState != RealmsMainScreen$LayoutState.LOADING;
      this.header.setJoinRealmButtonActive(this.activeLayoutState != RealmsMainScreen$LayoutState.LOADING);
      this.playButton.active = serverSelected && server.shouldPlayButtonBeActive();
      if (!this.playButton.active && serverSelected && server.state == RealmsServer$State.CLOSED) {
         this.playButton.setTooltip(Tooltip.create(RealmsServer.WORLD_CLOSED_COMPONENT));
      }

      this.renewButton.active = serverSelected && this.shouldRenewButtonBeActive(server);
      this.leaveButton.active = serverSelected && this.shouldLeaveButtonBeActive(server);
      this.configureButton.active = serverSelected && this.shouldConfigureButtonBeActive(server);
   }

   private boolean shouldRenewButtonBeActive(final RealmsServer server) {
      return server.expired && isSelfOwnedServer(server);
   }

   private boolean shouldConfigureButtonBeActive(final RealmsServer server) {
      return isSelfOwnedServer(server) && server.state != RealmsServer$State.UNINITIALIZED;
   }

   private boolean shouldLeaveButtonBeActive(final RealmsServer server) {
      return !isSelfOwnedServer(server);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.dataSubscription != null) {
         this.dataSubscription.tick();
      }
   }

   public static void refreshPendingInvites() {
      Minecraft.getInstance().realmsDataFetcher().pendingInvitesTask.reset();
   }

   public static void refreshServerList() {
      Minecraft.getInstance().realmsDataFetcher().serverListUpdateTask.reset();
   }

   private void debugRefreshDataFetchers() {
      for (DataFetcher$Task<?> task : this.minecraft.realmsDataFetcher().getTasks()) {
         task.reset();
      }
   }

   private DataFetcher$Subscription initDataFetcher(final RealmsDataFetcher dataSource) {
      DataFetcher$Subscription result = dataSource.dataFetcher.createSubscription();
      result.subscribe(dataSource.serverListUpdateTask, updatedServers -> {
         this.serverList.updateServersList(updatedServers.serverList());
         this.availableSnapshotServers = updatedServers.availableSnapshotServers();
         this.refreshListAndLayout();
         this.openPdpIfNoRealms();
         boolean ownsNonExpiredRealmServer = false;

         for (RealmsServer retrievedServer : this.serverList) {
            if (this.isSelfOwnedNonExpiredServer(retrievedServer)) {
               ownsNonExpiredRealmServer = true;
            }
         }

         if (!regionsPinged && ownsNonExpiredRealmServer) {
            regionsPinged = true;
            this.pingRegions();
         }
      });
      callRealmsClient(RealmsClient::getNotifications, retrievedNotifications -> {
         this.notifications.clear();
         this.notifications.addAll(retrievedNotifications);

         for (RealmsNotification notification : retrievedNotifications) {
            if (notification instanceof RealmsNotification$InfoPopup popup) {
               PopupScreen popupScreen = popup.buildScreen(this, this::dismissNotification);
               if (popupScreen != null) {
                  this.minecraft.gui.setScreen(popupScreen);
                  this.markNotificationsAsSeen(List.of(notification));
                  break;
               }
            }
         }

         if (!this.notifications.isEmpty() && this.activeLayoutState != RealmsMainScreen$LayoutState.LOADING) {
            this.refreshListAndLayout();
         }
      });
      result.subscribe(dataSource.pendingInvitesTask, numberOfPendingInvites -> {
         this.header.setPendingInvites(numberOfPendingInvites);
         if (numberOfPendingInvites > 0 && this.inviteNarrationLimiter.tryAcquire(1)) {
            this.minecraft.getNarrator().saySystemNow(Component.translatable("mco.configure.world.invite.narration", numberOfPendingInvites));
         }
      });
      result.subscribe(dataSource.trialAvailabilityTask, this::setTrialAvailable);
      result.subscribe(dataSource.onlinePlayersTask, playerList -> this.onlinePlayersPerRealm = playerList);
      result.subscribe(dataSource.newsTask, news -> {
         dataSource.newsManager.updateUnreadNews(news);
         this.header.setNews(dataSource.newsManager.newsLink(), dataSource.newsManager.hasUnreadNews());
      });
      return result;
   }

   private void markNotificationsAsSeen(final Collection<RealmsNotification> notifications) {
      List<UUID> seenNotifications = new ArrayList<>(notifications.size());

      for (RealmsNotification notification : notifications) {
         if (!notification.seen() && !this.handledSeenNotifications.contains(notification.uuid())) {
            seenNotifications.add(notification.uuid());
         }
      }

      if (!seenNotifications.isEmpty()) {
         callRealmsClient(realmsClient -> {
            realmsClient.notificationsSeen(seenNotifications);
            return null;
         }, ignored -> this.handledSeenNotifications.addAll(seenNotifications));
      }
   }

   private static <T> void callRealmsClient(final RealmsMainScreen$RealmsCall<T> supplier, final Consumer<T> callback) {
      Minecraft minecraft = Minecraft.getInstance();
      CompletableFuture.<T>supplyAsync(() -> {
         try {
            return supplier.request(RealmsClient.getOrCreate(minecraft));
         } catch (RealmsServiceException var3) {
            throw new RuntimeException(var3);
         }
      }, Util.nonCriticalIoPool()).thenAcceptAsync(callback, minecraft).exceptionally(e -> {
         LOGGER.error("Failed to execute call to Realms Service", e);
         return null;
      });
   }

   private void refreshListAndLayout() {
      this.realmSelectionList.refreshEntries(this);
      this.updateLayout();
      this.updateButtonStates();
   }

   private void openPdpIfNoRealms() {
      if (this.redirectToPdpIfNoRealms) {
         this.redirectToPdpIfNoRealms = false;
         if (this.serverList.isEmpty() && this.availableSnapshotServers.isEmpty() && this.minecraft.gui.screen() == this) {
            this.minecraft
               .gui
               .setScreen(new RealmsPdpScreen(this.lastScreen, this.trialAvailable, this::openJoinRealmScreen, () -> this.minecraft.gui.setScreen(this)));
         }
      }
   }

   private void setTrialAvailable(final boolean trialAvailable) {
      if (this.trialAvailable != trialAvailable) {
         this.trialAvailable = trialAvailable;
         this.addRealmButton = this.createAddRealmButton();
         this.updateLayout(this.activeLayoutState, true);
         this.updateButtonStates();
      }
   }

   private void pingRegions() {
      Util.nonCriticalIoPool().execute(() -> {
         List<RegionPingResult> regionPingResultList = Ping.pingAllRegions();
         RealmsClient client = RealmsClient.getOrCreate();
         PingResult pingResult = new PingResult(regionPingResultList, this.getOwnedNonExpiredRealmIds());

         try {
            client.sendPingResults(pingResult);
         } catch (Throwable var5) {
            LOGGER.warn("Could not send ping result to Realms: ", var5);
         }
      });
   }

   private List<Long> getOwnedNonExpiredRealmIds() {
      List<Long> ids = Lists.newArrayList();

      for (RealmsServer server : this.serverList) {
         if (this.isSelfOwnedNonExpiredServer(server)) {
            ids.add(server.id);
         }
      }

      return ids;
   }

   private void onRenew(@Nullable final RealmsServer server) {
      if (server != null) {
         CommonLinks$ExtensionReference reference = server.expiredTrial
            ? CommonLinks$ExtensionReference.EXPIRED_TRIAL
            : CommonLinks$ExtensionReference.EXPIRED_REALM;
         URI extensionUrl = CommonLinks.extendRealms(server.remoteSubscriptionId, this.minecraft.getUser().getProfileId(), reference);
         this.minecraft.gui.setScreen(new ConfirmLinkScreen(result -> {
            if (result) {
               Blaze3D.openUri(extensionUrl);
            } else {
               this.minecraft.gui.setScreen(this);
            }
         }, extensionUrl, true));
      }
   }

   private void configureClicked(@Nullable final RealmsServer selectedServer) {
      if (selectedServer != null && this.minecraft.isLocalPlayer(selectedServer.ownerUUID)) {
         this.minecraft.gui.setScreen(new RealmsConfigureWorldScreen(this, selectedServer.id));
      }
   }

   private void leaveClicked(@Nullable final RealmsServer selectedServer) {
      if (selectedServer != null && !this.minecraft.isLocalPlayer(selectedServer.ownerUUID)) {
         Component popupMessage = Component.translatable("mco.configure.world.leave.question.line1");
         this.minecraft.gui.setScreen(RealmsPopups.infoPopupScreen(this, popupMessage, var2x -> this.leaveServer(selectedServer)));
      }
   }

   @Nullable
   private RealmsServer getSelectedServer() {
      return this.realmSelectionList.getSelected() instanceof RealmsMainScreen$ServerEntry entry ? entry.getServer() : null;
   }

   private void leaveServer(final RealmsServer server) {
      new RealmsMainScreen$1(this, "Realms-leave-server", server).start();
      this.minecraft.gui.setScreen(this);
   }

   private void dismissNotification(final UUID uuid) {
      callRealmsClient(realmsClient -> {
         realmsClient.notificationsDismiss(List.of(uuid));
         return null;
      }, ignored -> {
         this.notifications.removeIf(notification -> notification.dismissable() && uuid.equals(notification.uuid()));
         this.refreshListAndLayout();
      });
   }

   public void resetScreen() {
      this.realmSelectionList.setSelected(null);
      refreshServerList();
   }

   @Override
   public Component getNarrationMessage() {
      return (Component)(switch (this.activeLayoutState) {
         case LOADING -> CommonComponents.joinForNarration(super.getNarrationMessage(), LOADING_TEXT);
         case NO_REALMS -> CommonComponents.joinForNarration(super.getNarrationMessage(), NO_REALMS_TEXT);
         case LIST -> super.getNarrationMessage();
      });
   }

   @Override
   public void extractRenderState(final GuiGraphicsExtractor graphics, final int xm, final int ym, final float a) {
      super.extractRenderState(graphics, xm, ym, a);
      if (isSnapshot()) {
         graphics.text(this.font, "Minecraft " + SharedConstants.getCurrentVersion().name(), 2, this.height - 10, -1);
      }

      switch (RealmsClient.ENVIRONMENT) {
         case STAGE:
            this.extractEnvironment(graphics, "STAGE!", -256);
            break;
         case LOCAL:
            this.extractEnvironment(graphics, "LOCAL!", -8388737);
      }
   }

   private void openRealmsPdp() {
      this.minecraft.gui.setScreen(new RealmsPdpScreen(this, this.trialAvailable, this::openJoinRealmScreen));
   }

   private void openJoinRealmScreen() {
      Screen lastScreen = this.minecraft.gui.screen();
      if (lastScreen != null) {
         this.minecraft.gui.setScreen(new RealmsJoinRealmWithCodeScreen(lastScreen, () -> {
            this.minecraft.gui.setScreen(this);
            this.resetScreen();
         }));
      }
   }

   public static void play(@Nullable final RealmsServer server, final Screen cancelScreen) {
      play(server, cancelScreen, false);
   }

   public static void play(@Nullable final RealmsServer server, final Screen cancelScreen, final boolean skipCompatibility) {
      if (server != null) {
         if (!isSnapshot() || skipCompatibility || server.isMinigameActive()) {
            Minecraft.getInstance().gui.setScreen(new RealmsLongRunningMcoTaskScreen(cancelScreen, new GetServerDetailsTask(cancelScreen, server)));
            return;
         }

         switch (server.compatibility) {
            case COMPATIBLE:
               Minecraft.getInstance().gui.setScreen(new RealmsLongRunningMcoTaskScreen(cancelScreen, new GetServerDetailsTask(cancelScreen, server)));
               break;
            case UNVERIFIABLE:
               confirmToPlay(
                  server,
                  cancelScreen,
                  Component.translatable("mco.compatibility.unverifiable.title").withColor(-171),
                  Component.translatable("mco.compatibility.unverifiable.message"),
                  CommonComponents.GUI_CONTINUE
               );
               break;
            case NEEDS_DOWNGRADE:
               confirmToPlay(
                  server,
                  cancelScreen,
                  Component.translatable("selectWorld.backupQuestion.downgrade").withColor(-2142128),
                  Component.translatable(
                     "mco.compatibility.downgrade.description",
                     Component.literal(server.activeVersion).withColor(-171),
                     Component.literal(SharedConstants.getCurrentVersion().name()).withColor(-171)
                  ),
                  Component.translatable("mco.compatibility.downgrade")
               );
               break;
            case NEEDS_UPGRADE:
               upgradeRealmAndPlay(server, cancelScreen);
               break;
            case INCOMPATIBLE:
               Minecraft.getInstance()
                  .gui
                  .setScreen(
                     new PopupScreen$Builder(cancelScreen, INCOMPATIBLE_POPUP_TITLE)
                        .addMessage(
                           Component.translatable(
                              "mco.compatibility.incompatible.series.popup.message",
                              Component.literal(server.activeVersion).withColor(-171),
                              Component.literal(SharedConstants.getCurrentVersion().name()).withColor(-171)
                           )
                        )
                        .addButton(CommonComponents.GUI_BACK, PopupScreen::onClose)
                        .build()
                  );
               break;
            case RELEASE_TYPE_INCOMPATIBLE:
               Minecraft.getInstance()
                  .gui
                  .setScreen(
                     new PopupScreen$Builder(cancelScreen, INCOMPATIBLE_POPUP_TITLE)
                        .addMessage(INCOMPATIBLE_RELEASE_TYPE_POPUP_MESSAGE)
                        .addButton(CommonComponents.GUI_BACK, PopupScreen::onClose)
                        .build()
                  );
         }
      }
   }

   private static void confirmToPlay(
      final RealmsServer server, final Screen lastScreen, final Component title, final Component message, final Component confirmButton
   ) {
      Minecraft.getInstance().gui.setScreen(new PopupScreen$Builder(lastScreen, title).addMessage(message).addButton(confirmButton, var2 -> {
         Minecraft.getInstance().gui.setScreen(new RealmsLongRunningMcoTaskScreen(lastScreen, new GetServerDetailsTask(lastScreen, server)));
         refreshServerList();
      }).addButton(CommonComponents.GUI_CANCEL, PopupScreen::onClose).build());
   }

   private static void upgradeRealmAndPlay(final RealmsServer server, final Screen cancelScreen) {
      Component title = Component.translatable("mco.compatibility.upgrade.title").withColor(-171);
      Component confirmButton = Component.translatable("mco.compatibility.upgrade");
      Component serverVersion = Component.literal(server.activeVersion).withColor(-171);
      Component clientVersion = Component.literal(SharedConstants.getCurrentVersion().name()).withColor(-171);
      Component message = isSelfOwnedServer(server)
         ? Component.translatable("mco.compatibility.upgrade.description", serverVersion, clientVersion)
         : Component.translatable("mco.compatibility.upgrade.friend.description", serverVersion, clientVersion);
      confirmToPlay(server, cancelScreen, title, message, confirmButton);
   }

   public static Component getVersionComponent(final String version, final boolean isCompatible) {
      return getVersionComponent(version, isCompatible ? -8355712 : -2142128);
   }

   public static Component getVersionComponent(final String version, final int color) {
      return (Component)(StringUtils.isBlank(version) ? CommonComponents.EMPTY : Component.literal(version).withColor(color));
   }

   public static Component getGameModeComponent(final int gameMode, final boolean hardcore) {
      return (Component)(hardcore ? Component.translatable("gameMode.hardcore").withColor(-65536) : GameType.byId(gameMode).getLongDisplayName());
   }

   private static boolean isSelfOwnedServer(final RealmsServer serverData) {
      return Minecraft.getInstance().isLocalPlayer(serverData.ownerUUID);
   }

   private boolean isSelfOwnedNonExpiredServer(final RealmsServer serverData) {
      return isSelfOwnedServer(serverData) && !serverData.expired;
   }

   private void extractEnvironment(final GuiGraphicsExtractor graphics, final String text, final int color) {
      graphics.pose().pushMatrix();
      graphics.pose().translate(this.width / 2 - 25, 20.0F);
      graphics.pose().rotate((float) (-Math.PI / 9));
      graphics.pose().scale(1.5F, 1.5F);
      graphics.text(this.font, text, 0, 0, color);
      graphics.pose().popMatrix();
   }
}
