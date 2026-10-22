package com.mojang.realmsclient.dto;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class GuardedSerializer$1 implements ExclusionStrategy {
   public boolean shouldSkipClass(final Class<?> clazz) {
      return false;
   }

   public boolean shouldSkipField(final FieldAttributes field) {
      return field.getAnnotation(Exclude.class) != null;
   }
}
