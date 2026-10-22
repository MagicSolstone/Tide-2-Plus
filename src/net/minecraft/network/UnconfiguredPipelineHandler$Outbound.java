package net.minecraft.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.EncoderException;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.network.protocol.Packet;

public class UnconfiguredPipelineHandler$Outbound extends ChannelOutboundHandlerAdapter {
   public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
      if (msg instanceof Packet) {
         ReferenceCountUtil.release(msg);
         throw new EncoderException("Pipeline has no outbound protocol configured, can't process packet " + msg);
      } else {
         if (msg instanceof UnconfiguredPipelineHandler$OutboundConfigurationTask configurationTask) {
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
}
