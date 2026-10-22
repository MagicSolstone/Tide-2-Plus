package net.minecraft.util;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.stream.Stream;

class ExtraCodecs$3 extends MapCodec<E> {
   ExtraCodecs$3(final MapCodec val$compressed, final MapCodec val$normal) {
      this.val$compressed = val$compressedx;
      this.val$normal = val$normalx;
   }

   public <T> RecordBuilder<T> encode(final E input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
      return ops.compressMaps() ? this.val$compressed.encode(input, ops, prefix) : this.val$normal.encode(input, ops, prefix);
   }

   public <T> DataResult<E> decode(final DynamicOps<T> ops, final MapLike<T> input) {
      return ops.compressMaps() ? this.val$compressed.decode(ops, input) : this.val$normal.decode(ops, input);
   }

   public <T> Stream<T> keys(final DynamicOps<T> ops) {
      return this.val$compressed.keys(ops);
   }

   public String toString() {
      return this.val$normal + " orCompressed " + this.val$compressed;
   }
}
