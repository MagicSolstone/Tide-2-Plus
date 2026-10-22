package net.minecraft.network;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;

public class UnconfiguredPipelineHandler {
   public static <T extends PacketListener> UnconfiguredPipelineHandler$InboundConfigurationTask setupInboundProtocol(final ProtocolInfo<T> protocolInfo) {
      return setupInboundHandler(new PacketDecoder<T>(protocolInfo));
   }

   private static UnconfiguredPipelineHandler$InboundConfigurationTask setupInboundHandler(final ChannelInboundHandler newHandler) {
      return ctx -> {
         ctx.pipeline().replace(ctx.name(), "decoder", newHandler);
         ctx.channel().config().setAutoRead(true);
      };
   }

   public static <T extends PacketListener> UnconfiguredPipelineHandler$OutboundConfigurationTask setupOutboundProtocol(final ProtocolInfo<T> codecData) {
      return setupOutboundHandler(new PacketEncoder<T>(codecData));
   }

   private static UnconfiguredPipelineHandler$OutboundConfigurationTask setupOutboundHandler(final ChannelOutboundHandler newHandler) {
      return ctx -> ctx.pipeline().replace(ctx.name(), "encoder", newHandler);
   }
}
