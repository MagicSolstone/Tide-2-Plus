package net.minecraft.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

class ExtraCodecs$2 implements Codec<E> {
   ExtraCodecs$2(final Codec val$compressed, final Codec val$normal) {
      this.val$compressed = val$compressedx;
      this.val$normal = val$normalx;
   }

   public <T> DataResult<T> encode(final E input, final DynamicOps<T> ops, final T prefix) {
      return ops.compressMaps() ? this.val$compressed.encode(input, ops, prefix) : this.val$normal.encode(input, ops, prefix);
   }

   public <T> DataResult<Pair<E, T>> decode(final DynamicOps<T> ops, final T input) {
      return ops.compressMaps() ? this.val$compressed.decode(ops, input) : this.val$normal.decode(ops, input);
   }

   @Override
   public String toString() {
      return this.val$normal + " orCompressed " + this.val$compressed;
   }
}
