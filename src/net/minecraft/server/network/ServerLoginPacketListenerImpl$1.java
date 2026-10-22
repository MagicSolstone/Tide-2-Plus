package net.minecraft.server.network;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.services.ProfileResult;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Objects;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

class ServerLoginPacketListenerImpl$1 extends Thread {
   ServerLoginPacketListenerImpl$1(final ServerLoginPacketListenerImpl this$0, String name, final String val$digest) {
      this.val$digest = val$digestx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(name);
   }

   @Override
   public void run() {
      String name = Objects.requireNonNull(this.this$0.requestedUsername, "Player name not initialized");

      try {
         ProfileResult result = this.this$0.server.services().sessionService().hasJoinedServer(name, this.val$digest, this.getAddress());
         if (result != null) {
            GameProfile profile = result.profile();
            ServerLoginPacketListenerImpl.LOGGER.info("UUID of player {} is {}", profile.name(), profile.id());
            this.this$0.serverActivityMonitor.reportLoginActivity();
            this.this$0.startClientVerification(profile);
         } else if (this.this$0.server.isSingleplayer()) {
            ServerLoginPacketListenerImpl.LOGGER.warn("Failed to verify username but will let them in anyway!");
            this.this$0.startClientVerification(UUIDUtil.createOfflineProfile(name));
         } else {
            this.this$0.disconnect(Component.translatable("multiplayer.disconnect.unverified_username"));
            ServerLoginPacketListenerImpl.LOGGER.error("Username '{}' tried to join with an invalid session", name);
         }
      } catch (AuthenticationUnavailableException var4) {
         if (this.this$0.server.isSingleplayer()) {
            ServerLoginPacketListenerImpl.LOGGER.warn("Authentication servers are down but will let them in anyway!");
            this.this$0.startClientVerification(UUIDUtil.createOfflineProfile(name));
         } else {
            this.this$0.disconnect(Component.translatable("multiplayer.disconnect.authservers_down"));
            ServerLoginPacketListenerImpl.LOGGER.error("Couldn't verify username because servers are unavailable");
         }
      }
   }

   @Nullable
   private InetAddress getAddress() {
      return this.this$0.server.getPreventProxyConnections() && this.this$0.connection.getRemoteAddress() instanceof InetSocketAddress inetSocketAddress
         ? inetSocketAddress.getAddress()
         : null;
   }
}
