package net.minecraft.server.network;

import io.netty.channel.Channel;
import io.netty.channel.IoHandlerFactory;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollIoHandler;

class EventLoopGroupHolder$2 extends EventLoopGroupHolder {
   EventLoopGroupHolder$2(final String type, final Class channelCls, final Class serverChannelCls) {
      super(type, channelCls, serverChannelCls);
   }

   @Override
   protected IoHandlerFactory ioHandlerFactory() {
      return EpollIoHandler.newFactory();
   }
}
