package net.minecraft.network.chat;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapEncoder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

class ComponentSerialization$FuzzyCodec<T> extends MapCodec<T> {
   private final Collection<MapCodec<? extends T>> codecs;
   private final Function<T, ? extends MapEncoder<? extends T>> encoderGetter;

   public ComponentSerialization$FuzzyCodec(final Collection<MapCodec<? extends T>> codecs, final Function<T, ? extends MapEncoder<? extends T>> encoderGetter) {
      this.codecs = codecs;
      this.encoderGetter = encoderGetter;
   }

   public <S> DataResult<T> decode(final DynamicOps<S> ops, final MapLike<S> input) {
      for (MapDecoder<? extends T> codec : this.codecs) {
         DataResult<? extends T> result = codec.decode(ops, input);
         if (result.result().isPresent()) {
            return (DataResult<T>)result;
         }
      }

      return DataResult.error(() -> "No matching codec found");
   }

   public <S> RecordBuilder<S> encode(final T input, final DynamicOps<S> ops, final RecordBuilder<S> prefix) {
      MapEncoder<T> encoder = (MapEncoder<T>)this.encoderGetter.apply(input);
      return encoder.encode(input, ops, prefix);
   }

   public <S> Stream<S> keys(final DynamicOps<S> ops) {
      return this.codecs.stream().flatMap(c -> c.keys(ops)).distinct();
   }

   public String toString() {
      return "FuzzyCodec[" + this.codecs + "]";
   }
}
