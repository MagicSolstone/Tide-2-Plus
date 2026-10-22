package net.minecraft.server.network;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.IoHandlerFactory;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.ThreadFactory;
import org.jspecify.annotations.Nullable;

public abstract class EventLoopGroupHolder {
   private static final EventLoopGroupHolder NIO = new EventLoopGroupHolder$1("NIO", NioSocketChannel.class, NioServerSocketChannel.class);
   private static final EventLoopGroupHolder EPOLL = new EventLoopGroupHolder$2("Epoll", EpollSocketChannel.class, EpollServerSocketChannel.class);
   private static final EventLoopGroupHolder KQUEUE = new EventLoopGroupHolder$3("Kqueue", KQueueSocketChannel.class, KQueueServerSocketChannel.class);
   private static final EventLoopGroupHolder LOCAL = new EventLoopGroupHolder$4("Local", LocalChannel.class, LocalServerChannel.class);
   private final String type;
   private final Class<? extends Channel> channelCls;
   private final Class<? extends ServerChannel> serverChannelCls;
   @Nullable
   private volatile EventLoopGroup group;

   public static EventLoopGroupHolder remote(final boolean allowNativeTransport) {
      if (allowNativeTransport) {
         if (KQueue.isAvailable()) {
            return KQUEUE;
         }

         if (Epoll.isAvailable()) {
            return EPOLL;
         }
      }

      return NIO;
   }

   public static EventLoopGroupHolder local() {
      return LOCAL;
   }

   private EventLoopGroupHolder(final String type, final Class<? extends Channel> channelCls, final Class<? extends ServerChannel> serverChannelCls) {
      this.type = type;
      this.channelCls = channelCls;
      this.serverChannelCls = serverChannelCls;
   }

   private ThreadFactory createThreadFactory() {
      return new ThreadFactoryBuilder().setNameFormat("Netty " + this.type + " IO #%d").setDaemon(true).build();
   }

   protected abstract IoHandlerFactory ioHandlerFactory();

   private EventLoopGroup createEventLoopGroup() {
      return new MultiThreadIoEventLoopGroup(this.createThreadFactory(), this.ioHandlerFactory());
   }

   public EventLoopGroup eventLoopGroup() {
      EventLoopGroup result = this.group;
      if (result == null) {
         synchronized (this) {
            result = this.group;
            if (result == null) {
               result = this.createEventLoopGroup();
               this.group = result;
            }
         }
      }

      return result;
   }

   public Class<? extends Channel> channelCls() {
      return this.channelCls;
   }

   public Class<? extends ServerChannel> serverChannelCls() {
      return this.serverChannelCls;
   }
}
