package net.minecraft.server.jsonrpc.methods;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.jsonrpc.api.PlayerDto;
import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

public class PlayerService {
   private static final Component DEFAULT_KICK_MESSAGE = Component.translatable("multiplayer.disconnect.kicked");

   public static List<PlayerDto> get(final MinecraftApi minecraftApi) {
      return minecraftApi.playerListService().getPlayers().stream().map(PlayerDto::from).toList();
   }

   public static List<PlayerDto> kick(final MinecraftApi minecraftApi, final List<PlayerService$KickDto> kick, final ClientInfo clientInfo) {
      List<PlayerDto> kicked = new ArrayList<>();

      for (PlayerService$KickDto kickDto : kick) {
         ServerPlayer serverPlayer = getServerPlayer(minecraftApi, kickDto.player());
         if (serverPlayer != null) {
            minecraftApi.playerListService().remove(serverPlayer, clientInfo);
            serverPlayer.connection.disconnect(kickDto.message.flatMap(Message::asComponent).orElse(DEFAULT_KICK_MESSAGE));
            kicked.add(kickDto.player());
         }
      }

      return kicked;
   }

   @Nullable
   private static ServerPlayer getServerPlayer(final MinecraftApi minecraftApi, final PlayerDto playerDto) {
      if (playerDto.id().isPresent()) {
         return minecraftApi.playerListService().getPlayer(playerDto.id().get());
      } else {
         return playerDto.name().isPresent() ? minecraftApi.playerListService().getPlayerByName(playerDto.name().get()) : null;
      }
   }
}
