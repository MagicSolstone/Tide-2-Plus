package net.minecraft.client.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface LegacyServerPinger$Output {
   void handleResponse(int protocolVersion, String gameVersion, String motd, int players, int maxPlayers);
}
