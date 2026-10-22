package com.mojang.realmsclient.util;

import com.google.gson.annotations.SerializedName;
import com.mojang.realmsclient.dto.ReflectionBasedSerialization;
import java.net.URI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class RealmsPersistence$RealmsPersistenceData implements ReflectionBasedSerialization {
   @SerializedName("newsLink")
   @Nullable
   public URI newsLink;
   @SerializedName("hasUnreadNews")
   public boolean hasUnreadNews;
}
