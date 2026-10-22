package com.mojang.realmsclient.dto;

import com.google.gson.annotations.SerializedName;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record RealmTierConfigurationDto$RealmTierRangeDto(
   @SerializedName("min") int min,
   @SerializedName("max") int max,
   @SerializedName("defaultValue") int defaultValue,
   @Nullable @SerializedName("current") Integer current
) implements ReflectionBasedSerialization {
}
