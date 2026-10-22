package net.minecraft.gametest.framework;

import java.util.Objects;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.notifications.NotificationService;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.PlayerDataStorage;

class GameTestServer$1 extends PlayerList {
   GameTestServer$1(
      final GameTestServer this$0,
      final MinecraftServer server,
      final LayeredRegistryAccess registries,
      final PlayerDataStorage playerIo,
      final NotificationService notificationService
   ) {
      Objects.requireNonNull(this$0);
      super(server, registries, playerIo, notificationService);
   }
}
