package net.minecraft.util;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.function.Function;
import java.util.stream.Stream;

class ExtraCodecs$1ContextRetrievalCodec extends MapCodec<E> {
   ExtraCodecs$1ContextRetrievalCodec(final Function val$getter) {
      this.val$getter = val$getterx;
   }

   public <T> RecordBuilder<T> encode(final E input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
      return prefix;
   }

   public <T> DataResult<E> decode(final DynamicOps<T> ops, final MapLike<T> input) {
      return (DataResult<E>)this.val$getter.apply(ops);
   }

   public String toString() {
      return "ContextRetrievalCodec[" + this.val$getter + "]";
   }

   public <T> Stream<T> keys(final DynamicOps<T> ops) {
      return Stream.empty();
   }
}
