package net.minecraft.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class StringRepresentable$StringRepresentableCodec<S extends StringRepresentable> implements Codec<S> {
   private final Codec<S> codec;

   public StringRepresentable$StringRepresentableCodec(final S[] valueArray, final Function<String, S> nameResolver, final ToIntFunction<S> idResolver) {
      this.codec = ExtraCodecs.orCompressed(
         Codec.stringResolver(StringRepresentable::getSerializedName, nameResolver),
         ExtraCodecs.idResolverCodec(idResolver, i -> i >= 0 && i < valueArray.length ? valueArray[i] : null, -1)
      );
   }

   public <T> DataResult<Pair<S, T>> decode(final DynamicOps<T> ops, final T input) {
      return this.codec.decode(ops, input);
   }

   public <T> DataResult<T> encode(final S input, final DynamicOps<T> ops, final T prefix) {
      return this.codec.encode(input, ops, prefix);
   }
}
