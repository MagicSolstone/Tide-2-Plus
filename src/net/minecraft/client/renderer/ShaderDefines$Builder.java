package net.minecraft.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableMap.Builder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ShaderDefines$Builder {
   private final Builder<String, String> values = ImmutableMap.builder();
   private final com.google.common.collect.ImmutableSet.Builder<String> flags = ImmutableSet.builder();

   private ShaderDefines$Builder() {
   }

   public ShaderDefines$Builder define(final String key, final String value) {
      if (value.isBlank()) {
         throw new IllegalArgumentException("Cannot define empty string");
      } else {
         this.values.put(key, escapeNewLines(value));
         return this;
      }
   }

   private static String escapeNewLines(final String value) {
      return value.replaceAll("\n", "\\\\\n");
   }

   public ShaderDefines$Builder define(final String key, final float value) {
      this.values.put(key, String.valueOf(value));
      return this;
   }

   public ShaderDefines$Builder define(final String key, final int value) {
      this.values.put(key, String.valueOf(value));
      return this;
   }

   public ShaderDefines$Builder define(final String key) {
      this.flags.add(key);
      return this;
   }

   public ShaderDefines build() {
      return new ShaderDefines(this.values.build(), this.flags.build());
   }
}
