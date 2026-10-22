package net.minecraft.server.jsonrpc.methods;

import com.google.common.net.InetAddresses;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.network.chat.Component;
import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.level.ServerPlayer;

public class IpBanlistService {
   private static final String BAN_SOURCE = "Management server";

   public static List<IpBanlistService$IpBanDto> get(final MinecraftApi minecraftApi) {
      return minecraftApi.banListService().getIpBanEntries().stream().map(IpBanlistService$IpBan::from).map(IpBanlistService$IpBanDto::from).toList();
   }

   public static List<IpBanlistService$IpBanDto> add(
      final MinecraftApi minecraftApi, final List<IpBanlistService$IncomingIpBanDto> bans, final ClientInfo clientInfo
   ) {
      bans.stream()
         .map(ban -> banIp(minecraftApi, ban, clientInfo))
         .flatMap(Collection::stream)
         .forEach(player -> player.connection.disconnect(Component.translatable("multiplayer.disconnect.ip_banned")));
      return get(minecraftApi);
   }

   private static List<ServerPlayer> banIp(final MinecraftApi minecraftApi, final IpBanlistService$IncomingIpBanDto ban, final ClientInfo clientInfo) {
      IpBanlistService$IpBan ipBan = ban.toIpBan();
      if (ipBan != null) {
         return banIp(minecraftApi, ipBan, clientInfo);
      } else {
         if (ban.player().isPresent()) {
            Optional<ServerPlayer> player = minecraftApi.playerListService().getPlayer(ban.player().get().id(), ban.player().get().name());
            if (player.isPresent()) {
               return banIp(minecraftApi, ban.toIpBan(player.get()), clientInfo);
            }
         }

         return List.of();
      }
   }

   private static List<ServerPlayer> banIp(final MinecraftApi minecraftApi, final IpBanlistService$IpBan ban, final ClientInfo clientInfo) {
      minecraftApi.banListService().addIpBan(ban.toIpBanEntry(), clientInfo);
      return minecraftApi.playerListService().getPlayersWithAddress(ban.ip());
   }

   public static List<IpBanlistService$IpBanDto> clear(final MinecraftApi minecraftApi, final ClientInfo clientInfo) {
      minecraftApi.banListService().clearIpBans(clientInfo);
      return get(minecraftApi);
   }

   public static List<IpBanlistService$IpBanDto> remove(final MinecraftApi minecraftApi, final List<String> ban, final ClientInfo clientInfo) {
      ban.forEach(ip -> minecraftApi.banListService().removeIpBan(ip, clientInfo));
      return get(minecraftApi);
   }

   public static List<IpBanlistService$IpBanDto> set(final MinecraftApi minecraftApi, final List<IpBanlistService$IpBanDto> ips, final ClientInfo clientInfo) {
      Set<IpBanlistService$IpBan> finalBanlist = ips.stream()
         .filter(ban -> InetAddresses.isInetAddress(ban.ip()))
         .map(IpBanlistService$IpBanDto::toIpBan)
         .collect(Collectors.toSet());
      Set<IpBanlistService$IpBan> currentBans = minecraftApi.banListService()
         .getIpBanEntries()
         .stream()
         .map(IpBanlistService$IpBan::from)
         .collect(Collectors.toSet());
      currentBans.stream().filter(ban -> !finalBanlist.contains(ban)).forEach(ban -> minecraftApi.banListService().removeIpBan(ban.ip(), clientInfo));
      finalBanlist.stream().filter(ban -> !currentBans.contains(ban)).forEach(ban -> minecraftApi.banListService().addIpBan(ban.toIpBanEntry(), clientInfo));
      finalBanlist.stream()
         .filter(ban -> !currentBans.contains(ban))
         .flatMap(ban -> minecraftApi.playerListService().getPlayersWithAddress(ban.ip()).stream())
         .forEach(player -> player.connection.disconnect(Component.translatable("multiplayer.disconnect.ip_banned")));
      return get(minecraftApi);
   }
}
