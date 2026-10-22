package net.minecraft.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import net.minecraft.network.protocol.PacketFlow;

class Connection$3 extends ChannelInitializer<Channel> {
   Connection$3(final Connection val$connection) {
      this.val$connection = val$connectionx;
   }

   protected void initChannel(final Channel channel) {
      ChannelPipeline pipeline = channel.pipeline();
      Connection.configureInMemoryPipeline(pipeline, PacketFlow.CLIENTBOUND);
      this.val$connection.configurePacketHandler(pipeline);
   }
}
