package net.minecraft.client.multiplayer;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.Connection;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
import net.minecraft.network.protocol.status.ClientStatusPacketListener;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.EventLoopGroupHolder;
import net.minecraft.server.players.NameAndId;
import net.minecraft.util.Util;

@Environment(EnvType.CLIENT)
class ServerStatusPinger$1 implements ClientStatusPacketListener {
   private boolean success;
   private boolean receivedPing;
   private long pingStart;

   ServerStatusPinger$1(
      final ServerStatusPinger this$0,
      final Connection val$connection,
      final ServerData val$data,
      final Runnable val$onPersistentDataChange,
      final Runnable val$onPongResponse,
      final InetSocketAddress val$address,
      final ServerAddress val$rawAddress,
      final EventLoopGroupHolder val$eventLoopGroupHolder
   ) {
      this.val$connection = val$connectionx;
      this.val$data = val$datax;
      this.val$onPersistentDataChange = val$onPersistentDataChangex;
      this.val$onPongResponse = val$onPongResponsex;
      this.val$address = val$addressx;
      this.val$rawAddress = val$rawAddressx;
      this.val$eventLoopGroupHolder = val$eventLoopGroupHolderx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void handleStatusResponse(final ClientboundStatusResponsePacket packet) {
      if (this.receivedPing) {
         this.val$connection.disconnect(Component.translatable("multiplayer.status.unrequested"));
      } else {
         this.receivedPing = true;
         ServerStatus status = packet.status();
         this.val$data.motd = sanitizeDescription(status.description());
         status.version().ifPresentOrElse(version -> {
            data.version = Component.literal(version.name());
            data.protocol = version.protocol();
         }, () -> {
            data.version = Component.translatable("multiplayer.status.old");
            data.protocol = 0;
         });
         status.players().ifPresentOrElse(players -> {
            data.status = ServerStatusPinger.formatPlayerCount(players.online(), players.max());
            data.players = players;
            if (!players.sample().isEmpty()) {
               List<Component> playerNames = new ArrayList<>(players.sample().size());

               for (NameAndId profile : players.sample()) {
                  Component playerName;
                  if (profile.equals(MinecraftServer.ANONYMOUS_PLAYER_PROFILE)) {
                     playerName = Component.translatable("multiplayer.status.anonymous_player");
                  } else {
                     playerName = Component.literal(profile.name());
                  }

                  playerNames.add(playerName);
               }

               if (players.sample().size() < players.online()) {
                  playerNames.add(Component.translatable("multiplayer.status.and_more", players.online() - players.sample().size()));
               }

               data.playerList = playerNames;
            } else {
               data.playerList = List.of();
            }
         }, () -> data.status = Component.translatable("multiplayer.status.unknown").withStyle(ChatFormatting.DARK_GRAY));
         status.favicon().ifPresent(newIcon -> {
            if (!Arrays.equals(newIcon.iconBytes(), data.getIconBytes())) {
               data.setIconBytes(ServerData.validateIcon(newIcon.iconBytes()));
               onPersistentDataChange.run();
            }
         });
         this.pingStart = Util.getMillis();
         this.val$connection.send(new ServerboundPingRequestPacket(this.pingStart));
         this.success = true;
      }
   }

   private static Component sanitizeDescription(final Component original) {
      try {
         return ComponentUtils.resolve(ServerStatusPinger.DESCRIPTION_SANITIZE_CONTEXT, original);
      } catch (CommandSyntaxException var2) {
         ServerStatusPinger.LOGGER.warn("Failed to sanitize status {}", original, var2);
         return Component.empty();
      }
   }

   @Override
   public void handlePongResponse(final ClientboundPongResponsePacket packet) {
      long then = this.pingStart;
      long now = Util.getMillis();
      this.val$data.ping = now - then;
      this.val$connection.disconnect(Component.translatable("multiplayer.status.finished"));
      this.val$onPongResponse.run();
   }

   @Override
   public void onDisconnect(final DisconnectionDetails details) {
      if (!this.success) {
         this.this$0.onPingFailed(details.reason(), this.val$data);
         this.this$0.pingLegacyServer(this.val$address, this.val$rawAddress, this.val$data, this.val$eventLoopGroupHolder);
      }
   }

   @Override
   public boolean isAcceptingMessages() {
      return this.val$connection.isConnected();
   }
}
