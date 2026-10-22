package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

record StructuresBecomeConfiguredFix$Conversion(Map<String, String> biomeMapping, String fallback) {
   public static StructuresBecomeConfiguredFix$Conversion trivial(final String result) {
      return new StructuresBecomeConfiguredFix$Conversion(Map.of(), result);
   }

   public static StructuresBecomeConfiguredFix$Conversion biomeMapped(final Map<List<String>, String> mapping, final String fallback) {
      return new StructuresBecomeConfiguredFix$Conversion(unpack(mapping), fallback);
   }

   private static Map<String, String> unpack(final Map<List<String>, String> packed) {
      Builder<String, String> builder = ImmutableMap.builder();

      for (Entry<List<String>, String> entry : packed.entrySet()) {
         entry.getKey().forEach(k -> builder.put(k, entry.getValue()));
      }

      return builder.build();
   }
}
