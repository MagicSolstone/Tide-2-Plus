package net.minecraft.client.multiplayer;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import io.netty.bootstrap.Bootstrap;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ResolutionContext;
import net.minecraft.network.chat.ResolutionContext$LimitBehavior;
import net.minecraft.network.chat.contents.objects.PlayerSprite;
import net.minecraft.network.protocol.status.ClientStatusPacketListener;
import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;
import net.minecraft.server.network.EventLoopGroupHolder;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class ServerStatusPinger {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Component CANT_CONNECT_MESSAGE = Component.translatable("multiplayer.status.cannot_connect").withColor(-65536);
   private static final ResolutionContext DESCRIPTION_SANITIZE_CONTEXT = ResolutionContext.builder()
      .withObjectInfoValidator(description -> !(description instanceof PlayerSprite))
      .setDepthLimit(16)
      .setDepthLimitBehavior(ResolutionContext$LimitBehavior.DISCARD_REMAINING)
      .build();
   private final List<Connection> connections = Collections.synchronizedList(Lists.newArrayList());

   public void pingServer(
      final ServerData data, final Runnable onPersistentDataChange, final Runnable onPongResponse, final EventLoopGroupHolder eventLoopGroupHolder
   ) throws UnknownHostException {
      ServerAddress rawAddress = ServerAddress.parseString(data.ip);
      Optional<InetSocketAddress> resolvedAddress = ServerNameResolver.DEFAULT.resolveAddress(rawAddress).map(ResolvedServerAddress::asInetSocketAddress);
      if (resolvedAddress.isEmpty()) {
         this.onPingFailed(ConnectScreen.UNKNOWN_HOST_MESSAGE, data);
      } else {
         InetSocketAddress address = resolvedAddress.get();
         Connection connection = Connection.connectToServer(address, eventLoopGroupHolder, null);
         this.connections.add(connection);
         data.motd = Component.translatable("multiplayer.status.pinging");
         data.playerList = Collections.emptyList();
         ClientStatusPacketListener listener = new ServerStatusPinger$1(
            this, connection, data, onPersistentDataChange, onPongResponse, address, rawAddress, eventLoopGroupHolder
         );

         try {
            connection.initiateServerboundStatusConnection(rawAddress.getHost(), rawAddress.getPort(), listener);
            connection.send(ServerboundStatusRequestPacket.INSTANCE);
         } catch (Throwable var11) {
            LOGGER.error("Failed to ping server {}", rawAddress, var11);
         }
      }
   }

   private void onPingFailed(final Component reason, final ServerData data) {
      LOGGER.error("Can't ping {}: {}", data.ip, reason.getString());
      data.motd = CANT_CONNECT_MESSAGE;
      data.status = CommonComponents.EMPTY;
   }

   private void pingLegacyServer(
      final InetSocketAddress resolvedAddress, final ServerAddress rawAddress, final ServerData data, final EventLoopGroupHolder eventLoopGroupHolder
   ) {
      ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group(eventLoopGroupHolder.eventLoopGroup()))
               .handler(new ServerStatusPinger$2(this, rawAddress, data)))
            .channel(eventLoopGroupHolder.channelCls()))
         .connect(resolvedAddress.getAddress(), resolvedAddress.getPort());
   }

   public static Component formatPlayerCount(final int curPlayers, final int maxPlayers) {
      Component current = Component.literal(Integer.toString(curPlayers)).withStyle(ChatFormatting.GRAY);
      Component max = Component.literal(Integer.toString(maxPlayers)).withStyle(ChatFormatting.GRAY);
      return Component.translatable("multiplayer.status.player_count", current, max).withStyle(ChatFormatting.DARK_GRAY);
   }

   public void tick() {
      synchronized (this.connections) {
         Iterator<Connection> iterator = this.connections.iterator();

         while (iterator.hasNext()) {
            Connection connection = iterator.next();
            if (connection.isConnected()) {
               connection.tick();
            } else {
               iterator.remove();
               connection.handleDisconnection();
            }
         }
      }
   }

   public void removeAll() {
      synchronized (this.connections) {
         Iterator<Connection> iterator = this.connections.iterator();

         while (iterator.hasNext()) {
            Connection connection = iterator.next();
            if (connection.isConnected()) {
               iterator.remove();
               connection.disconnect(Component.translatable("multiplayer.status.cancelled"));
            }
         }
      }
   }
}
