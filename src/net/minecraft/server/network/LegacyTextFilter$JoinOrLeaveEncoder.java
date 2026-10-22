package net.minecraft.server.network;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

@FunctionalInterface
interface LegacyTextFilter$JoinOrLeaveEncoder {
   JsonObject encode(GameProfile profile);
}
