package net.minecraft.client.multiplayer.resolver;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class AddressCheck$1 implements AddressCheck {
   AddressCheck$1(final ImmutableList val$blockLists) {
      this.val$blockLists = val$blockListsx;
   }

   @Override
   public boolean isAllowed(final ResolvedServerAddress address) {
      String hostName = address.getHostName();
      String hostIp = address.getHostIp();
      return this.val$blockLists.stream().noneMatch(p -> p.test(hostName) || p.test(hostIp));
   }

   @Override
   public boolean isAllowed(final ServerAddress address) {
      String hostName = address.getHost();
      return this.val$blockLists.stream().noneMatch(p -> p.test(hostName));
   }
}
