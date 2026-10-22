package net.minecraft.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;

class ExtraCodecs$5 implements Decoder<A> {
   ExtraCodecs$5(final Codec val$codec) {
      this.val$codec = val$codecx;
   }

   public <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
      try {
         return this.val$codec.decode(ops, input);
      } catch (Exception var4) {
         return DataResult.error(() -> "Caught exception decoding " + input + ": " + var4.getMessage());
      }
   }
}
