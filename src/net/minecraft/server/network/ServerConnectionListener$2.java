package net.minecraft.server.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import java.util.Objects;
import net.minecraft.SharedConstants;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;

class ServerConnectionListener$2 extends ChannelInitializer<Channel> {
   ServerConnectionListener$2(final ServerConnectionListener this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   protected void initChannel(final Channel channel) {
      Connection connection = new Connection(PacketFlow.SERVERBOUND);
      connection.setListenerForServerboundHandshake(new MemoryServerHandshakePacketListenerImpl(this.this$0.server, connection));
      this.this$0.connections.add(connection);
      ChannelPipeline pipeline = channel.pipeline();
      Connection.configureInMemoryPipeline(pipeline, PacketFlow.SERVERBOUND);
      if (SharedConstants.DEBUG_FAKE_LATENCY_MS > 0) {
         pipeline.addLast("latency", new ServerConnectionListener$LatencySimulator(SharedConstants.DEBUG_FAKE_LATENCY_MS, SharedConstants.DEBUG_FAKE_JITTER_MS));
      }

      connection.configurePacketHandler(pipeline);
   }
}
