package net.minecraft.server.jsonrpc.methods;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatus$Version;
import net.minecraft.server.jsonrpc.api.PlayerDto;
import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.level.ServerPlayer;

public class ServerStateService {
   public static ServerStateService$ServerState status(final MinecraftApi minecraftApi) {
      return !minecraftApi.serverStateService().isReady()
         ? ServerStateService$ServerState.NOT_STARTED
         : new ServerStateService$ServerState(true, PlayerService.get(minecraftApi), ServerStatus$Version.current());
   }

   public static boolean save(final MinecraftApi minecraftApi, final boolean flush, final ClientInfo clientInfo) {
      return minecraftApi.serverStateService().saveEverything(true, flush, true, clientInfo);
   }

   public static boolean stop(final MinecraftApi minecraftApi, final ClientInfo clientInfo) {
      minecraftApi.submit(() -> minecraftApi.serverStateService().halt(false, clientInfo));
      return true;
   }

   public static boolean systemMessage(final MinecraftApi minecraftApi, final ServerStateService$SystemMessage systemMessage, final ClientInfo clientInfo) {
      Component component = systemMessage.message().asComponent().orElse(null);
      if (component == null) {
         return false;
      } else {
         if (systemMessage.receivingPlayers().isPresent()) {
            if (systemMessage.receivingPlayers().get().isEmpty()) {
               return false;
            }

            for (PlayerDto playerDto : systemMessage.receivingPlayers().get()) {
               ServerPlayer player;
               if (playerDto.id().isPresent()) {
                  player = minecraftApi.playerListService().getPlayer(playerDto.id().get());
               } else {
                  if (!playerDto.name().isPresent()) {
                     continue;
                  }

                  player = minecraftApi.playerListService().getPlayerByName(playerDto.name().get());
               }

               if (player != null) {
                  player.sendSystemMessage(component, systemMessage.overlay());
               }
            }
         } else {
            minecraftApi.serverStateService().broadcastSystemMessage(component, systemMessage.overlay(), clientInfo);
         }

         return true;
      }
   }
}
