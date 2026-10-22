package com.mojang.realmsclient.client;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.dto.RegionPingResult;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Comparator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Util;
import org.apache.commons.io.IOUtils;

@Environment(EnvType.CLIENT)
public class Ping {
   public static List<RegionPingResult> ping(final Ping$Region... regions) {
      for (Ping$Region region : regions) {
         ping(region.endpoint);
      }

      List<RegionPingResult> results = Lists.newArrayList();

      for (Ping$Region region : regions) {
         results.add(new RegionPingResult(region.name, ping(region.endpoint)));
      }

      results.sort(Comparator.comparingInt(RegionPingResult::ping));
      return results;
   }

   private static int ping(final String host) {
      int timeout = 700;
      long sum = 0L;
      Socket socket = null;

      for (int i = 0; i < 5; i++) {
         try {
            SocketAddress sockAddr = new InetSocketAddress(host, 80);
            socket = new Socket();
            long t1 = now();
            socket.connect(sockAddr, 700);
            sum += now() - t1;
         } catch (Exception var12) {
            sum += 700L;
         } finally {
            IOUtils.closeQuietly(socket);
         }
      }

      return (int)(sum / 5.0);
   }

   private static long now() {
      return Util.getMillis();
   }

   public static List<RegionPingResult> pingAllRegions() {
      return ping(Ping$Region.values());
   }
}
