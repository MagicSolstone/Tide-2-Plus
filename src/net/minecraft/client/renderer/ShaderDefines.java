package net.minecraft.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record ShaderDefines(Map<String, String> values, Set<String> flags) {
   public static final ShaderDefines EMPTY = new ShaderDefines(Map.of(), Set.of());
   public static final Codec<ShaderDefines> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("values", Map.of()).forGetter(ShaderDefines::values),
            Codec.STRING.listOf().xmap(Set::copyOf, List::copyOf).optionalFieldOf("flags", Set.of()).forGetter(ShaderDefines::flags)
         )
         .apply(i, ShaderDefines::new)
   );

   public static ShaderDefines$Builder builder() {
      return new ShaderDefines$Builder();
   }

   public ShaderDefines withOverrides(final ShaderDefines defines) {
      if (this.isEmpty()) {
         return defines;
      } else if (defines.isEmpty()) {
         return this;
      } else {
         com.google.common.collect.ImmutableMap.Builder<String, String> newValues = ImmutableMap.builderWithExpectedSize(
            this.values.size() + defines.values.size()
         );
         newValues.putAll(this.values);
         newValues.putAll(defines.values);
         com.google.common.collect.ImmutableSet.Builder<String> newFlags = ImmutableSet.builderWithExpectedSize(this.flags.size() + defines.flags.size());
         newFlags.addAll(this.flags);
         newFlags.addAll(defines.flags);
         return new ShaderDefines(newValues.buildKeepingLast(), newFlags.build());
      }
   }

   public String asSourceDirectives() {
      StringBuilder directives = new StringBuilder();

      for (Entry<String, String> entry : this.values.entrySet()) {
         String key = entry.getKey();
         String value = entry.getValue();
         directives.append("#define ").append(key).append(" ").append(value).append('\n');
      }

      for (String flag : this.flags) {
         directives.append("#define ").append(flag).append('\n');
      }

      return directives.toString();
   }

   public boolean isEmpty() {
      return this.values.isEmpty() && this.flags.isEmpty();
   }
}
