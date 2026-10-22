package net.minecraft.network.codec;

import java.util.Map;
import java.util.function.IntFunction;

class ByteBufCodecs$28 implements StreamCodec<B, M> {
   ByteBufCodecs$28(final int val$maxSize, final StreamCodec val$keyCodec, final StreamCodec val$valueCodec, final IntFunction val$constructor) {
      this.val$maxSize = val$maxSizex;
      this.val$keyCodec = val$keyCodecx;
      this.val$valueCodec = val$valueCodecx;
      this.val$constructor = val$constructorx;
   }

   public void encode(final B output, final M map) {
      ByteBufCodecs.writeCount(output, map.size(), this.val$maxSize);
      map.forEach((k, v) -> {
         keyCodec.encode(output, k);
         valueCodec.encode(output, v);
      });
   }

   public M decode(final B input) {
      int count = ByteBufCodecs.readCount(input, this.val$maxSize);
      M result = (M)((Map)this.val$constructor.apply(Math.min(count, 65536)));

      for (int i = 0; i < count; i++) {
         K key = (K)this.val$keyCodec.decode(input);
         V value = (V)this.val$valueCodec.decode(input);
         result.put(key, value);
      }

      return result;
   }
}
