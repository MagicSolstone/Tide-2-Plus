package net.minecraft.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.Objects;
import net.minecraft.nbt.Tag;

class EncoderCache$2 implements Codec<A> {
   EncoderCache$2(final EncoderCache this$0, final Codec val$codec) {
      this.val$codec = val$codecx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   public <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
      return this.val$codec.decode(ops, input);
   }

   public <T> DataResult<T> encode(final A input, final DynamicOps<T> ops, final T prefix) {
      return ((DataResult)this.this$0.cache.getUnchecked(new EncoderCache$Key(this.val$codec, input, ops)))
         .map(value -> value instanceof Tag tag ? tag.copy() : value);
   }
}
