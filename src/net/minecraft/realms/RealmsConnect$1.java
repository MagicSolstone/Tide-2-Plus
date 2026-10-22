package net.minecraft.realms;

import com.mojang.realmsclient.dto.RealmsServer;
import java.net.InetSocketAddress;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.LevelLoadTracker;
import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
import net.minecraft.client.quickplay.QuickPlayLog$Type;
import net.minecraft.client.resources.server.ServerPackManager$PackPromptStatus;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.network.EventLoopGroupHolder;

@Environment(EnvType.CLIENT)
class RealmsConnect$1 extends Thread {
   RealmsConnect$1(
      final RealmsConnect this$0, String name, final String val$hostname, final int val$port, final Minecraft val$minecraft, final RealmsServer val$server
   ) {
      this.val$hostname = val$hostnamex;
      this.val$port = val$portx;
      this.val$minecraft = val$minecraftx;
      this.val$server = val$serverx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(name);
   }

   @Override
   public void run() {
      InetSocketAddress address = null;

      try {
         address = new InetSocketAddress(this.val$hostname, this.val$port);
         if (this.this$0.aborted) {
            return;
         }

         this.this$0.connection = Connection.connectToServer(
            address, EventLoopGroupHolder.remote(this.val$minecraft.options.useNativeTransport()), this.val$minecraft.getDebugOverlay().getBandwidthLogger()
         );
         if (this.this$0.aborted) {
            return;
         }

         ClientHandshakePacketListenerImpl clientHandshakePacketListener = new ClientHandshakePacketListenerImpl(
            this.this$0.connection,
            this.val$minecraft,
            this.val$server.toServerData(this.val$hostname),
            this.this$0.onlineScreen,
            false,
            null,
            status -> {},
            new LevelLoadTracker(),
            null
         );
         if (this.val$server.isMinigameActive()) {
            clientHandshakePacketListener.setMinigameName(this.val$server.minigameName);
         }

         if (this.this$0.aborted) {
            return;
         }

         this.this$0.connection.initiateServerboundPlayConnection(this.val$hostname, this.val$port, clientHandshakePacketListener);
         if (this.this$0.aborted) {
            return;
         }

         this.this$0.connection.send(new ServerboundHelloPacket(this.val$minecraft.getUser().getName(), this.val$minecraft.getUser().getProfileId()));
         this.val$minecraft.updateReportEnvironment(ReportEnvironment.realm(this.val$server));
         this.val$minecraft
            .quickPlayLog()
            .setWorldData(QuickPlayLog$Type.REALMS, String.valueOf(this.val$server.id), Objects.requireNonNullElse(this.val$server.name, "unknown"));
         this.val$minecraft.getDownloadedPackSource().configureForServerControl(this.this$0.connection, ServerPackManager$PackPromptStatus.ALLOWED);
      } catch (Exception var5) {
         this.val$minecraft.getDownloadedPackSource().cleanupAfterDisconnect();
         if (this.this$0.aborted) {
            return;
         }

         RealmsConnect.LOGGER.error("Couldn't connect to world", var5);
         String message = var5.toString();
         if (address != null) {
            String filter = address + ":" + this.val$port;
            message = message.replaceAll(filter, "");
         }

         DisconnectedScreen screen = new DisconnectedScreen(
            this.this$0.onlineScreen,
            Component.translatable("mco.connect.failed"),
            Component.translatable("disconnect.genericReason", message),
            CommonComponents.GUI_BACK
         );
         this.val$minecraft.execute(() -> minecraft.gui.setScreen(screen));
      }
   }
}
