package net.minecraft.server.jsonrpc;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import java.util.Objects;
import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.jsonrpc.websocket.JsonToWebSocketEncoder;
import net.minecraft.server.jsonrpc.websocket.WebSocketToJsonCodec;

class ManagementServer$1 extends ChannelInitializer<Channel> {
   ManagementServer$1(
      final ManagementServer this$0, final SslContext val$sslContext, final MinecraftApi val$minecraftApi, final JsonRpcLogger val$jsonrpcLogger
   ) {
      this.val$sslContext = val$sslContextx;
      this.val$minecraftApi = val$minecraftApix;
      this.val$jsonrpcLogger = val$jsonrpcLoggerx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   protected void initChannel(final Channel channel) {
      try {
         channel.config().setOption(ChannelOption.TCP_NODELAY, true);
      } catch (ChannelException var3) {
      }

      ChannelPipeline pipeline = channel.pipeline();
      if (this.val$sslContext != null) {
         pipeline.addLast(new ChannelHandler[]{this.val$sslContext.newHandler(channel.alloc())});
      }

      pipeline.addLast(new ChannelHandler[]{new HttpServerCodec()})
         .addLast(new ChannelHandler[]{new HttpObjectAggregator(65536)})
         .addLast(new ChannelHandler[]{this.this$0.authenticationHandler})
         .addLast(new ChannelHandler[]{new WebSocketServerProtocolHandler("/")})
         .addLast(new ChannelHandler[]{new WebSocketFrameAggregator(65536)})
         .addLast(new ChannelHandler[]{new WebSocketToJsonCodec()})
         .addLast(new ChannelHandler[]{new JsonToWebSocketEncoder()})
         .addLast(new ChannelHandler[]{new Connection(channel, this.this$0, this.val$minecraftApi, this.val$jsonrpcLogger)});
   }
}
