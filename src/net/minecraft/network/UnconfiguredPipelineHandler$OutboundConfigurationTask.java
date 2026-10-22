package net.minecraft.network;

import io.netty.channel.ChannelHandlerContext;

@FunctionalInterface
public interface UnconfiguredPipelineHandler$OutboundConfigurationTask {
   void run(ChannelHandlerContext ctx);

   default UnconfiguredPipelineHandler$OutboundConfigurationTask andThen(final UnconfiguredPipelineHandler$OutboundConfigurationTask otherTask) {
      return ctx -> {
         this.run(ctx);
         otherTask.run(ctx);
      };
   }
}
