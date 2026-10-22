package net.minecraft.server.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.util.Objects;
import net.minecraft.network.Connection;
import net.minecraft.network.RateKickingConnection;
import net.minecraft.network.protocol.PacketFlow;

class ServerConnectionListener$1 extends ChannelInitializer<Channel> {
   ServerConnectionListener$1(final ServerConnectionListener this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   protected void initChannel(final Channel channel) {
      try {
         channel.config().setOption(ChannelOption.TCP_NODELAY, true);
      } catch (ChannelException var5) {
      }

      ChannelPipeline pipeline = channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30));
      if (this.this$0.server.repliesToStatus()) {
         pipeline.addLast("legacy_query", new LegacyQueryHandler(this.this$0.getServer()));
      }

      Connection.configureSerialization(pipeline, PacketFlow.SERVERBOUND, false, null);
      int rateLimitPacketsPerSecond = this.this$0.server.getRateLimitPacketsPerSecond();
      Connection connection = (Connection)(rateLimitPacketsPerSecond > 0
         ? new RateKickingConnection(rateLimitPacketsPerSecond)
         : new Connection(PacketFlow.SERVERBOUND));
      this.this$0.pendingConnections.add(connection);
      connection.configurePacketHandler(pipeline);
      connection.setListenerForServerboundHandshake(new ServerHandshakePacketListenerImpl(this.this$0.server, connection));
   }
}
