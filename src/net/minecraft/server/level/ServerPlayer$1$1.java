package net.minecraft.server.level;

import com.google.common.cache.CacheLoader;
import com.google.common.hash.HashCode;
import com.mojang.serialization.DynamicOps;
import java.util.Objects;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.util.HashOps;

class ServerPlayer$1$1 extends CacheLoader<TypedDataComponent<?>, Integer> {
   private final DynamicOps<HashCode> registryHashOps;

   ServerPlayer$1$1(final ServerPlayer$1 this$1) {
      Objects.requireNonNull(this$1);
      this.this$1 = this$1;
      super();
      this.registryHashOps = this.this$1.this$0.registryAccess().createSerializationContext(HashOps.CRC32C_INSTANCE);
   }

   public Integer load(final TypedDataComponent<?> component) {
      return ((HashCode)component.encodeValue(this.registryHashOps).getOrThrow(msg -> new IllegalArgumentException("Failed to hash " + component + ": " + msg)))
         .asInt();
   }
}
