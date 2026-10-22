package net.minecraft.server.network;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import java.util.List;
import java.util.concurrent.TimeUnit;

class ServerConnectionListener$LatencySimulator extends ChannelInboundHandlerAdapter {
   private static final Timer TIMER = new HashedWheelTimer(new ThreadFactoryBuilder().setNameFormat("Latency Simulator #%d").setDaemon(true).build());
   private final int delay;
   private final int jitter;
   private final List<ServerConnectionListener$LatencySimulator$DelayedMessage> queuedMessages = Lists.newArrayList();

   public ServerConnectionListener$LatencySimulator(final int delay, final int jitter) {
      this.delay = delay;
      this.jitter = jitter;
   }

   public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
      this.delayDownstream(ctx, msg);
   }

   private void delayDownstream(final ChannelHandlerContext ctx, final Object msg) {
      int sendDelay = this.delay + (int)(Math.random() * this.jitter);
      this.queuedMessages.add(new ServerConnectionListener$LatencySimulator$DelayedMessage(ctx, msg));
      TIMER.newTimeout(this::onTimeout, sendDelay, TimeUnit.MILLISECONDS);
   }

   private void onTimeout(final Timeout timeout) {
      ServerConnectionListener$LatencySimulator$DelayedMessage next = this.queuedMessages.remove(0);
      next.ctx.fireChannelRead(next.msg);
   }
}
