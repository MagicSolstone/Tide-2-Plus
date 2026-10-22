package com.mojang.realmsclient.dto;

import com.google.gson.annotations.SerializedName;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record RealmsDescriptionDto(@Nullable @SerializedName("name") String name, @SerializedName("description") String description)
   implements ReflectionBasedSerialization {
}
