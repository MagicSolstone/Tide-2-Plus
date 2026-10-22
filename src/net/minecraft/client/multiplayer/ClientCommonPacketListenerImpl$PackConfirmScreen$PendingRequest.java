package net.minecraft.client.multiplayer;

import java.net.URL;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest(UUID id, URL url, String hash) {
}
