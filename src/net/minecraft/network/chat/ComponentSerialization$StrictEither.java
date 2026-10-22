package net.minecraft.network.chat;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.stream.Stream;

class ComponentSerialization$StrictEither<T> extends MapCodec<T> {
   private final String typeFieldName;
   private final MapCodec<T> typed;
   private final MapCodec<T> fuzzy;

   public ComponentSerialization$StrictEither(final String typeFieldName, final MapCodec<T> typed, final MapCodec<T> fuzzy) {
      this.typeFieldName = typeFieldName;
      this.typed = typed;
      this.fuzzy = fuzzy;
   }

   public <O> DataResult<T> decode(final DynamicOps<O> ops, final MapLike<O> input) {
      return input.get(this.typeFieldName) != null ? this.typed.decode(ops, input) : this.fuzzy.decode(ops, input);
   }

   public <O> RecordBuilder<O> encode(final T input, final DynamicOps<O> ops, final RecordBuilder<O> prefix) {
      return this.fuzzy.encode(input, ops, prefix);
   }

   public <T1> Stream<T1> keys(final DynamicOps<T1> ops) {
      return Stream.concat(this.typed.keys(ops), this.fuzzy.keys(ops)).distinct();
   }
}
