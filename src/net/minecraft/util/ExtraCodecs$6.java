package net.minecraft.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

class ExtraCodecs$6 extends MapCodec<V> {
   ExtraCodecs$6(final String val$typeKey, final String val$valueKey, final Codec val$typeCodec, final Function val$valueCodec, final Function val$typeGetter) {
      this.val$typeKey = val$typeKeyx;
      this.val$valueKey = val$valueKeyx;
      this.val$typeCodec = val$typeCodecx;
      this.val$valueCodec = val$valueCodecx;
      this.val$typeGetter = val$typeGetterx;
   }

   public <T> Stream<T> keys(final DynamicOps<T> ops) {
      return Stream.of((T[])(new Object[]{ops.createString(this.val$typeKey), ops.createString(this.val$valueKey)}));
   }

   public <T> DataResult<V> decode(final DynamicOps<T> ops, final MapLike<T> input) {
      T typeName = (T)input.get(this.val$typeKey);
      return typeName == null ? DataResult.error(() -> "Missing \"" + typeKey + "\" in: " + input) : this.val$typeCodec.decode(ops, typeName).flatMap(type -> {
         T value = Objects.requireNonNullElseGet((T)input.get(valueKey), ops::emptyMap);
         return ((Codec)valueCodec.apply(type.getFirst())).decode(ops, value).map(Pair::getFirst);
      });
   }

   public <T> RecordBuilder<T> encode(final V input, final DynamicOps<T> ops, final RecordBuilder<T> builder) {
      K type = (K)this.val$typeGetter.apply((T)input);
      builder.add(this.val$typeKey, this.val$typeCodec.encodeStart(ops, type));
      DataResult<T> parameters = this.encode((Codec)this.val$valueCodec.apply((T)type), input, ops);
      if (parameters.result().isEmpty() || !Objects.equals(parameters.result().get(), ops.emptyMap())) {
         builder.add(this.val$valueKey, parameters);
      }

      return builder;
   }

   private <T, V2 extends V> DataResult<T> encode(final Codec<V2> codec, final V input, final DynamicOps<T> ops) {
      return codec.encodeStart(ops, input);
   }
}
