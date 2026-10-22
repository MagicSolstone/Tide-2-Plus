package com.mojang.realmsclient.dto;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class GuardedSerializer {
   private static final ExclusionStrategy STRATEGY = new GuardedSerializer$1();
   private final Gson gson = new GsonBuilder().addSerializationExclusionStrategy(STRATEGY).addDeserializationExclusionStrategy(STRATEGY).create();

   public String toJson(final ReflectionBasedSerialization object) {
      return this.gson.toJson(object);
   }

   public String toJson(final JsonElement jsonElement) {
      return this.gson.toJson(jsonElement);
   }

   @Nullable
   public <T extends ReflectionBasedSerialization> T fromJson(final String contents, final Class<T> cls) {
      return (T)this.gson.fromJson(contents, cls);
   }
}
