package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.DecoderException;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.network.protocol.Packet;

public class UnconfiguredPipelineHandler$Inbound extends ChannelDuplexHandler {
   public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
      if (!(msg instanceof ByteBuf) && !(msg instanceof Packet)) {
         ctx.fireChannelRead(msg);
      } else {
         ReferenceCountUtil.release(msg);
         throw new DecoderException("Pipeline has no inbound protocol configured, can't process packet " + msg);
      }
   }

   public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
      if (msg instanceof UnconfiguredPipelineHandler$InboundConfigurationTask configurationTask) {
         try {
            configurationTask.run(ctx);
         } finally {
            ReferenceCountUtil.release(msg);
         }

         promise.setSuccess();
      } else {
         ctx.write(msg, promise);
      }
   }
}
