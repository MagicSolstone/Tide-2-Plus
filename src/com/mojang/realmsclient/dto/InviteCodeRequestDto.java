package com.mojang.realmsclient.dto;

import com.google.gson.annotations.SerializedName;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record InviteCodeRequestDto(
   @Nullable @SerializedName("linkId") String code,
   @SerializedName("realmId") long realmId,
   @SerializedName("enabled") boolean enabled,
   @Nullable @SerializedName("expirationDate") Long expirationDate
) implements ReflectionBasedSerialization {
}
