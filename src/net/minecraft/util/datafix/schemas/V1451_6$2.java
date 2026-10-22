package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.templates.Hook.HookFunction;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.Optional;

class V1451_6$2 implements HookFunction {
   public <T> T apply(final DynamicOps<T> ops, final T value) {
      Dynamic<T> input = new Dynamic(ops, value);
      Optional<Dynamic<T>> repackedId = input.get("CriteriaType")
         .get()
         .result()
         .flatMap(
            type -> {
               Optional<String> statType = type.get("type").asString().result();
               Optional<String> statId = type.get("id").asString().result();
               if (statType.isPresent() && statId.isPresent()) {
                  String unpackedType = statType.get();
                  return unpackedType.equals("_special")
                     ? Optional.of(input.createString(statId.get()))
                     : Optional.of(type.createString(V1451_6.packNamespacedWithDot(unpackedType) + ":" + V1451_6.packNamespacedWithDot(statId.get())));
               } else {
                  return Optional.empty();
               }
            }
         );
      return (T)((Dynamic)DataFixUtils.orElse(repackedId.map(id -> input.set("CriteriaName", id).remove("CriteriaType")), input)).getValue();
   }
}
