package net.minecraft.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import java.util.Objects;

class Connection$2 extends ChannelOutboundHandlerAdapter {
   Connection$2(final Connection this$0) {
      Objects.requireNonNull(this$0);
      super();
   }

   public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
      super.write(ctx, msg, promise);
   }
}
