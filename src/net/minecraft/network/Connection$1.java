package net.minecraft.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.minecraft.network.protocol.PacketFlow;

class Connection$1 extends ChannelInitializer<Channel> {
   Connection$1(final Connection val$connection) {
      this.val$connection = val$connectionx;
   }

   protected void initChannel(final Channel channel) {
      try {
         channel.config().setOption(ChannelOption.TCP_NODELAY, true);
      } catch (ChannelException var3) {
      }

      ChannelPipeline pipeline = channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30));
      Connection.configureSerialization(pipeline, PacketFlow.CLIENTBOUND, false, this.val$connection.bandwidthDebugMonitor);
      this.val$connection.configurePacketHandler(pipeline);
   }
}
