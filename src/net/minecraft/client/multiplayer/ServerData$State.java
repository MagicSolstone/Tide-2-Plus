package net.minecraft.client.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum ServerData$State {
   INITIAL,
   PINGING,
   UNREACHABLE,
   INCOMPATIBLE,
   SUCCESSFUL;
}
