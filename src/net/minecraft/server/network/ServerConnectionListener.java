package net.minecraft.server.network;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalServerChannel;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.server.MinecraftServer;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class ServerConnectionListener {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final MinecraftServer server;
   public volatile boolean running;
   @Nullable
   private volatile UUID sessionId;
   private final List<ChannelFuture> channels = Collections.synchronizedList(Lists.newArrayList());
   private final List<Connection> connections = Collections.synchronizedList(Lists.newArrayList());
   private final Queue<Connection> pendingConnections = new ConcurrentLinkedQueue<>();

   public ServerConnectionListener(final MinecraftServer server) {
      this.server = server;
      this.running = true;
   }

   public void startTcpServerListener(@Nullable final InetAddress address, final int port) throws IOException {
      synchronized (this.channels) {
         EventLoopGroupHolder eventLoopGroupHolder = EventLoopGroupHolder.remote(this.server.useNativeTransport());
         this.channels
            .add(
               ((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel(eventLoopGroupHolder.serverChannelCls()))
                     .childHandler(new ServerConnectionListener$1(this))
                     .group(eventLoopGroupHolder.eventLoopGroup())
                     .localAddress(address, port))
                  .bind()
                  .syncUninterruptibly()
            );
      }
   }

   public SocketAddress startMemoryChannel() {
      ChannelFuture newChannel;
      synchronized (this.channels) {
         newChannel = ((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel(EventLoopGroupHolder.local().serverChannelCls()))
               .childHandler(new ServerConnectionListener$2(this))
               .group(EventLoopGroupHolder.local().eventLoopGroup())
               .localAddress(LocalAddress.ANY))
            .bind()
            .syncUninterruptibly();
         this.channels.add(newChannel);
      }

      return newChannel.channel().localAddress();
   }

   public void stop() {
      this.running = false;

      for (ChannelFuture channel : this.channels) {
         try {
            channel.channel().close().sync();
         } catch (InterruptedException var4) {
            LOGGER.error("Interrupted whilst closing channel");
         }
      }
   }

   public void stopTcpServerListener() {
      synchronized (this.channels) {
         Iterator<ChannelFuture> iterator = this.channels.iterator();

         while (iterator.hasNext()) {
            ChannelFuture future = iterator.next();
            if (!(future.channel() instanceof LocalServerChannel)) {
               try {
                  future.channel().close().sync();
               } catch (InterruptedException var6) {
                  LOGGER.error("Interrupted whilst closing TCP listener");
               }

               iterator.remove();
            }
         }
      }
   }

   public void tick() {
      synchronized (this.connections) {
         this.addPendingConnections();
         Iterator<Connection> iterator = this.connections.iterator();

         while (iterator.hasNext()) {
            Connection connection = iterator.next();
            if (!connection.isConnecting()) {
               if (connection.isConnected()) {
                  try {
                     connection.tick();
                  } catch (Exception var9) {
                     if (connection.isMemoryConnection()) {
                        throw new ReportedException(CrashReport.forThrowable(var9, "Ticking memory connection"));
                     }

                     LOGGER.warn("Failed to handle packet for {}", connection.getLoggableAddress(this.server.logIPs()), var9);
                     Component component = Component.literal("Internal server error");
                     connection.send(new ClientboundDisconnectPacket(component), PacketSendListener.thenRun(() -> connection.disconnect(component)));
                     connection.setReadOnly();
                  }
               } else {
                  iterator.remove();
                  connection.handleDisconnection();
               }
            }
         }

         if (this.connections.isEmpty()) {
            synchronized (this) {
               this.sessionId = null;
            }
         }
      }
   }

   public MinecraftServer getServer() {
      return this.server;
   }

   private void addPendingConnections() {
      Connection connection;
      while ((connection = this.pendingConnections.poll()) != null) {
         this.connections.add(connection);
      }
   }

   public List<Connection> getConnections() {
      return this.connections;
   }

   public UUID getSessionId() {
      UUID uuid = this.sessionId;
      if (uuid != null) {
         return uuid;
      } else {
         synchronized (this) {
            uuid = this.sessionId;
            if (uuid == null) {
               uuid = UUID.randomUUID();
               this.sessionId = uuid;
            }

            return uuid;
         }
      }
   }
}
