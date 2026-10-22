package net.minecraft.util.datafix.schemas;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;

class NamespacedSchema$1 implements PrimitiveCodec<String> {
   public <T> DataResult<String> read(final DynamicOps<T> ops, final T input) {
      return ops.getStringValue(input).map(NamespacedSchema::ensureNamespaced);
   }

   public <T> T write(final DynamicOps<T> ops, final String value) {
      return (T)ops.createString(value);
   }

   @Override
   public String toString() {
      return "NamespacedString";
   }
}
