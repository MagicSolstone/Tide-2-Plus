package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.types.templates.Hook.HookFunction;
import com.mojang.serialization.DynamicOps;
import java.util.Map;
import java.util.Optional;

class V3818_5$1 implements HookFunction {
   public <T> T apply(final DynamicOps<T> ops, final T value) {
      Optional<String> maybePlainId = ops.getStringValue(value).result();
      return (T)(maybePlainId.isPresent() ? ops.createMap(Map.of(ops.createString("id"), value, ops.createString("count"), (T)ops.createInt(1))) : value);
   }
}
