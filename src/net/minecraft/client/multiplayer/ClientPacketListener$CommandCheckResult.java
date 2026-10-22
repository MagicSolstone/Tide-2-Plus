package net.minecraft.client.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
enum ClientPacketListener$CommandCheckResult {
   NO_ISSUES,
   PARSE_ERRORS,
   SIGNATURE_REQUIRED,
   PERMISSIONS_REQUIRED;
}
