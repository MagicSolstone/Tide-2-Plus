package net.minecraft.server.network;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

@FunctionalInterface
public interface ServerTextFilter$MessageEncoder {
   JsonObject encode(GameProfile profile, String message);
}
