package net.minecraft.server.jsonrpc.methods;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.network.chat.Component;
import net.minecraft.server.jsonrpc.api.PlayerDto;
import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.util.Util;

public class BanlistService {
   private static final String BAN_SOURCE = "Management server";

   public static List<BanlistService$UserBanDto> get(final MinecraftApi minecraftApi) {
      return minecraftApi.banListService()
         .getUserBanEntries()
         .stream()
         .filter(p -> p.getUser() != null)
         .map(BanlistService$UserBan::from)
         .map(BanlistService$UserBanDto::from)
         .toList();
   }

   public static List<BanlistService$UserBanDto> add(final MinecraftApi minecraftApi, final List<BanlistService$UserBanDto> bans, final ClientInfo clientInfo) {
      List<CompletableFuture<Optional<BanlistService$UserBan>>> fetch = bans.stream()
         .map(banx -> minecraftApi.playerListService().getUser(banx.player().id(), banx.player().name()).thenApply(u -> u.map(banx::toUserBan)))
         .toList();

      for (Optional<BanlistService$UserBan> ban : Util.sequence(fetch).join()) {
         if (!ban.isEmpty()) {
            BanlistService$UserBan userBan = ban.get();
            minecraftApi.banListService().addUserBan(userBan.toBanEntry(), clientInfo);
            ServerPlayer player = minecraftApi.playerListService().getPlayer(ban.get().player().id());
            if (player != null) {
               player.connection.disconnect(Component.translatable("multiplayer.disconnect.banned"));
            }
         }
      }

      return get(minecraftApi);
   }

   public static List<BanlistService$UserBanDto> clear(final MinecraftApi minecraftApi, final ClientInfo clientInfo) {
      minecraftApi.banListService().clearUserBans(clientInfo);
      return get(minecraftApi);
   }

   public static List<BanlistService$UserBanDto> remove(final MinecraftApi minecraftApi, final List<PlayerDto> remove, final ClientInfo clientInfo) {
      List<CompletableFuture<Optional<NameAndId>>> fetch = remove.stream()
         .map(playerDto -> minecraftApi.playerListService().getUser(playerDto.id(), playerDto.name()))
         .toList();

      for (Optional<NameAndId> user : Util.sequence(fetch).join()) {
         if (!user.isEmpty()) {
            minecraftApi.banListService().removeUserBan(user.get(), clientInfo);
         }
      }

      return get(minecraftApi);
   }

   public static List<BanlistService$UserBanDto> set(final MinecraftApi minecraftApi, final List<BanlistService$UserBanDto> bans, final ClientInfo clientInfo) {
      List<CompletableFuture<Optional<BanlistService$UserBan>>> fetch = bans.stream()
         .map(ban -> minecraftApi.playerListService().getUser(ban.player().id(), ban.player().name()).thenApply(u -> u.map(ban::toUserBan)))
         .toList();
      Set<BanlistService$UserBan> finalAllowList = Util.sequence(fetch).join().stream().flatMap(Optional::stream).collect(Collectors.toSet());
      Set<BanlistService$UserBan> currentAllowList = minecraftApi.banListService()
         .getUserBanEntries()
         .stream()
         .filter(entry -> entry.getUser() != null)
         .map(BanlistService$UserBan::from)
         .collect(Collectors.toSet());
      currentAllowList.stream()
         .filter(ban -> !finalAllowList.contains(ban))
         .forEach(ban -> minecraftApi.banListService().removeUserBan(ban.player(), clientInfo));
      finalAllowList.stream().filter(ban -> !currentAllowList.contains(ban)).forEach(ban -> {
         minecraftApi.banListService().addUserBan(ban.toBanEntry(), clientInfo);
         ServerPlayer player = minecraftApi.playerListService().getPlayer(ban.player().id());
         if (player != null) {
            player.connection.disconnect(Component.translatable("multiplayer.disconnect.banned"));
         }
      });
      return get(minecraftApi);
   }
}
