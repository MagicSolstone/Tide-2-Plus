package com.mojang.realmsclient.dto;

import com.google.gson.annotations.SerializedName;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record RealmTierConfigurationDto(
   @SerializedName("renderDistance") RealmTierConfigurationDto$RealmTierRangeDto renderDistance,
   @SerializedName("simDistance") RealmTierConfigurationDto$RealmTierRangeDto simDistance
) implements ReflectionBasedSerialization {
}
