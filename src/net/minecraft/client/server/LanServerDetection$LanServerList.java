package net.minecraft.client.server;

import com.google.common.collect.Lists;
import java.net.InetAddress;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class LanServerDetection$LanServerList {
   private final List<LanServer> servers = Lists.newArrayList();
   private boolean isDirty;

   @Nullable
   public synchronized List<LanServer> takeDirtyServers() {
      if (this.isDirty) {
         List<LanServer> newServers = List.copyOf(this.servers);
         this.isDirty = false;
         return newServers;
      } else {
         return null;
      }
   }

   public synchronized void addServer(final String pingData, final InetAddress socketAddress) {
      String motd = LanServerPinger.parseMotd(pingData);
      String address = LanServerPinger.parseAddress(pingData);
      if (address != null) {
         address = socketAddress.getHostAddress() + ":" + address;
         boolean found = false;

         for (LanServer server : this.servers) {
            if (server.getAddress().equals(address)) {
               server.updatePingTime();
               found = true;
               break;
            }
         }

         if (!found) {
            this.servers.add(new LanServer(motd, address));
            this.isDirty = true;
         }
      }
   }
}
