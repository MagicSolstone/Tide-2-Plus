package net.minecraft.network;

import io.netty.channel.ChannelHandlerContext;

@FunctionalInterface
public interface UnconfiguredPipelineHandler$InboundConfigurationTask {
   void run(ChannelHandlerContext ctx);

   default UnconfiguredPipelineHandler$InboundConfigurationTask andThen(final UnconfiguredPipelineHandler$InboundConfigurationTask otherTask) {
      return ctx -> {
         this.run(ctx);
         otherTask.run(ctx);
      };
   }
}
