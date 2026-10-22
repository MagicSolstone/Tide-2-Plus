package net.minecraft.client.multiplayer.resolver;

import java.net.InetSocketAddress;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class ResolvedServerAddress$1 implements ResolvedServerAddress {
   ResolvedServerAddress$1(final InetSocketAddress val$address) {
      this.val$address = val$addressx;
   }

   @Override
   public String getHostName() {
      return this.val$address.getAddress().getHostName();
   }

   @Override
   public String getHostIp() {
      return this.val$address.getAddress().getHostAddress();
   }

   @Override
   public int getPort() {
      return this.val$address.getPort();
   }

   @Override
   public InetSocketAddress asInetSocketAddress() {
      return this.val$address;
   }
}
