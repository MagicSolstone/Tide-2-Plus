package net.minecraft.client.multiplayer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatus$Players;

@Environment(EnvType.CLIENT)
class ServerStatusPinger$2 extends ChannelInitializer<Channel> {
   ServerStatusPinger$2(final ServerStatusPinger this$0, final ServerAddress val$rawAddress, final ServerData val$data) {
      this.val$rawAddress = val$rawAddressx;
      this.val$data = val$datax;
      Objects.requireNonNull(this$0);
      super();
   }

   protected void initChannel(final Channel channel) {
      try {
         channel.config().setOption(ChannelOption.TCP_NODELAY, true);
      } catch (ChannelException var3) {
      }

      channel.pipeline()
         .addLast(new ChannelHandler[]{new LegacyServerPinger(this.val$rawAddress, (protocolVersion, gameVersion, motd, players, maxPlayers) -> {
            data.setState(ServerData$State.INCOMPATIBLE);
            data.version = Component.literal(gameVersion);
            data.motd = Component.literal(motd);
            data.status = ServerStatusPinger.formatPlayerCount(players, maxPlayers);
            data.players = new ServerStatus$Players(maxPlayers, players, List.of());
         })});
   }
}
