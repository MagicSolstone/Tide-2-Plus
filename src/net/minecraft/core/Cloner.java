package net.minecraft.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JavaOps;

public class Cloner<T> {
   private final Codec<T> directCodec;

   private Cloner(final Codec<T> directCodec) {
      this.directCodec = directCodec;
   }

   public T clone(final T value, final HolderLookup$Provider from, final HolderLookup$Provider to) {
      DynamicOps<Object> sourceOps = from.createSerializationContext(JavaOps.INSTANCE);
      DynamicOps<Object> targetOps = to.createSerializationContext(JavaOps.INSTANCE);
      Object serialized = this.directCodec.encodeStart(sourceOps, value).getOrThrow(error -> new IllegalStateException("Failed to encode: " + error));
      return (T)this.directCodec.parse(targetOps, serialized).getOrThrow(error -> new IllegalStateException("Failed to decode: " + error));
   }
}
