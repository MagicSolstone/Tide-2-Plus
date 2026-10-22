package net.minecraft.util.datafix.schemas;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.templates.Hook.HookFunction;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import net.minecraft.resources.Identifier;

class V1451_6$1 implements HookFunction {
   public <T> T apply(final DynamicOps<T> ops, final T value) {
      Dynamic<T> input = new Dynamic(ops, value);
      return (T)((Dynamic)DataFixUtils.orElse(
            input.get("CriteriaName")
               .asString()
               .result()
               .map(name -> {
                  int colonPos = name.indexOf(58);
                  if (colonPos < 0) {
                     return Pair.of("_special", name);
                  } else {
                     try {
                        Identifier statType = Identifier.bySeparator(name.substring(0, colonPos), '.');
                        Identifier statId = Identifier.bySeparator(name.substring(colonPos + 1), '.');
                        return Pair.of(statType.toString(), statId.toString());
                     } catch (Exception var4) {
                        return Pair.of("_special", name);
                     }
                  }
               })
               .map(
                  explodedId -> input.set(
                     "CriteriaType",
                     input.createMap(
                        ImmutableMap.of(
                           input.createString("type"),
                           input.createString((String)explodedId.getFirst()),
                           input.createString("id"),
                           input.createString((String)explodedId.getSecond())
                        )
                     )
                  )
               ),
            input
         ))
         .getValue();
   }
}
