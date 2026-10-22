package net.minecraft.client.gui.screens;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.LevelLoadTracker;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerData$ServerPackStatus;
import net.minecraft.client.multiplayer.TransferState;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.client.resources.server.ServerPackManager$PackPromptStatus;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.login.LoginProtocols;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.network.EventLoopGroupHolder;

@Environment(EnvType.CLIENT)
class ConnectScreen$1 extends Thread {
   ConnectScreen$1(
      final ConnectScreen this$0,
      String name,
      final ServerAddress val$hostAndPort,
      final Minecraft val$minecraft,
      final ServerData val$server,
      final TransferState val$transferState
   ) {
      this.val$hostAndPort = val$hostAndPortx;
      this.val$minecraft = val$minecraftx;
      this.val$server = val$serverx;
      this.val$transferState = val$transferStatex;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(name);
   }

   @Override
   public void run() {
      InetSocketAddress address = null;

      try {
         if (this.this$0.aborted) {
            return;
         }

         Optional<InetSocketAddress> resolvedAddress = ServerNameResolver.DEFAULT
            .resolveAddress(this.val$hostAndPort)
            .map(ResolvedServerAddress::asInetSocketAddress);
         if (this.this$0.aborted) {
            return;
         }

         if (resolvedAddress.isEmpty()) {
            this.val$minecraft
               .execute(
                  () -> minecraft.gui.setScreen(new DisconnectedScreen(this.this$0.parent, this.this$0.connectFailedTitle, ConnectScreen.UNKNOWN_HOST_MESSAGE))
               );
            return;
         }

         address = resolvedAddress.get();
         Connection pendingConnection;
         synchronized (this.this$0) {
            if (this.this$0.aborted) {
               return;
            }

            pendingConnection = new Connection(PacketFlow.CLIENTBOUND);
            pendingConnection.setBandwidthLogger(this.val$minecraft.getDebugOverlay().getBandwidthLogger());
            this.this$0.channelFuture = Connection.connect(
               address, EventLoopGroupHolder.remote(this.val$minecraft.options.useNativeTransport()), pendingConnection
            );
         }

         this.this$0.channelFuture.syncUninterruptibly();
         synchronized (this.this$0) {
            if (this.this$0.aborted) {
               pendingConnection.disconnect(ConnectScreen.ABORT_CONNECTION);
               return;
            }

            this.this$0.connection = pendingConnection;
            this.val$minecraft
               .getDownloadedPackSource()
               .configureForServerControl(pendingConnection, convertPackStatus(this.val$server.getResourcePackStatus()));
         }

         this.this$0
            .connection
            .initiateServerboundPlayConnection(
               address.getHostName(),
               address.getPort(),
               LoginProtocols.SERVERBOUND,
               LoginProtocols.CLIENTBOUND,
               new ClientHandshakePacketListenerImpl(
                  this.this$0.connection,
                  this.val$minecraft,
                  this.val$server,
                  this.this$0.parent,
                  false,
                  null,
                  this.this$0::updateStatus,
                  new LevelLoadTracker(),
                  this.val$transferState
               ),
               this.val$transferState != null
            );
         this.this$0.connection.send(new ServerboundHelloPacket(this.val$minecraft.getUser().getName(), this.val$minecraft.getUser().getProfileId()));
      } catch (Exception var9) {
         if (this.this$0.aborted) {
            return;
         }

         Exception cause;
         if (var9.getCause() instanceof Exception originalCause) {
            cause = originalCause;
         } else {
            cause = var9;
         }

         ConnectScreen.LOGGER.error("Couldn't connect to server", var9);
         String message = address == null
            ? cause.getMessage()
            : cause.getMessage().replaceAll(address.getHostName() + ":" + address.getPort(), "").replaceAll(address.toString(), "");
         this.val$minecraft
            .execute(
               () -> minecraft.gui
                  .setScreen(
                     new DisconnectedScreen(this.this$0.parent, this.this$0.connectFailedTitle, Component.translatable("disconnect.genericReason", message))
                  )
            );
      }
   }

   private static ServerPackManager$PackPromptStatus convertPackStatus(final ServerData$ServerPackStatus resourcePackStatus) {
      return switch (resourcePackStatus) {
         case ENABLED -> ServerPackManager$PackPromptStatus.ALLOWED;
         case DISABLED -> ServerPackManager$PackPromptStatus.DECLINED;
         case PROMPT -> ServerPackManager$PackPromptStatus.PENDING;
      };
   }
}
