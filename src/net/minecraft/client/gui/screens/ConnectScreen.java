package net.minecraft.client.gui.screens;

import com.mojang.logging.LogUtils;
import io.netty.channel.ChannelFuture;
import java.util.concurrent.atomic.AtomicInteger;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.TransferState;
import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.quickplay.QuickPlay;
import net.minecraft.client.quickplay.QuickPlayLog$Type;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class ConnectScreen extends Screen {
   private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final long NARRATION_DELAY_MS = 2000L;
   public static final Component ABORT_CONNECTION = Component.translatable("connect.aborted");
   public static final Component UNKNOWN_HOST_MESSAGE = Component.translatable("disconnect.genericReason", Component.translatable("disconnect.unknownHost"));
   @Nullable
   private volatile Connection connection;
   @Nullable
   private ChannelFuture channelFuture;
   private volatile boolean aborted;
   private final Screen parent;
   private Component status = Component.translatable("connect.connecting");
   private long lastNarration = -1L;
   private final Component connectFailedTitle;

   private ConnectScreen(final Screen parent, final Component connectFailedTitle) {
      super(GameNarrator.NO_TITLE);
      this.parent = parent;
      this.connectFailedTitle = connectFailedTitle;
   }

   public static void startConnecting(
      final Screen parent,
      final Minecraft minecraft,
      final ServerAddress hostAndPort,
      final ServerData data,
      final boolean isQuickPlay,
      @Nullable final TransferState transferState
   ) {
      if (minecraft.gui.screen() instanceof ConnectScreen) {
         LOGGER.error("Attempt to connect while already connecting");
      } else {
         Component connectFailedTitle;
         if (transferState != null) {
            connectFailedTitle = CommonComponents.TRANSFER_CONNECT_FAILED;
         } else if (isQuickPlay) {
            connectFailedTitle = QuickPlay.ERROR_TITLE;
         } else {
            connectFailedTitle = CommonComponents.CONNECT_FAILED;
         }

         ConnectScreen screen = new ConnectScreen(parent, connectFailedTitle);
         if (transferState != null) {
            screen.updateStatus(Component.translatable("connect.transferring"));
         }

         minecraft.disconnectWithProgressScreen(false);
         minecraft.prepareForMultiplayer();
         minecraft.updateReportEnvironment(ReportEnvironment.thirdParty(data.ip));
         minecraft.quickPlayLog().setWorldData(QuickPlayLog$Type.MULTIPLAYER, data.ip, data.name);
         minecraft.gui.setScreen(screen);
         screen.connect(minecraft, hostAndPort, data, transferState);
      }
   }

   private void connect(final Minecraft minecraft, final ServerAddress hostAndPort, final ServerData server, @Nullable final TransferState transferState) {
      LOGGER.info("Connecting to {}, {}", hostAndPort.getHost(), hostAndPort.getPort());
      Thread thread = new ConnectScreen$1(this, "Server Connector #" + UNIQUE_THREAD_ID.incrementAndGet(), hostAndPort, minecraft, server, transferState);
      thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
      thread.start();
   }

   private void updateStatus(final Component status) {
      this.status = status;
   }

   @Override
   public void tick() {
      if (this.connection != null) {
         if (this.connection.isConnected()) {
            this.connection.tick();
         } else {
            this.connection.handleDisconnection();
         }
      }
   }

   @Override
   public boolean shouldCloseOnEsc() {
      return false;
   }

   @Override
   protected void init() {
      this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> {
         synchronized (this) {
            this.aborted = true;
            if (this.channelFuture != null) {
               this.channelFuture.cancel(true);
               this.channelFuture = null;
            }

            if (this.connection != null) {
               this.connection.disconnect(ABORT_CONNECTION);
            }
         }

         this.minecraft.gui.setScreen(this.parent);
      }).bounds(this.width / 2 - 100, this.height / 4 + 120 + 12, 200, 20).build());
   }

   @Override
   public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      super.extractRenderState(graphics, mouseX, mouseY, a);
      long current = Util.getMillis();
      if (current - this.lastNarration > 2000L) {
         this.lastNarration = current;
         this.minecraft.getNarrator().saySystemNow(Component.translatable("narrator.joining"));
      }

      graphics.centeredText(this.font, this.status, this.width / 2, this.height / 2 - 50, -1);
   }
}
