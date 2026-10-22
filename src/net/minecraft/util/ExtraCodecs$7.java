package net.minecraft.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import java.util.Optional;

class ExtraCodecs$7 implements Codec<Optional<A>> {
   ExtraCodecs$7(final Codec val$codec) {
      this.val$codec = val$codecx;
   }

   public <T> DataResult<Pair<Optional<A>, T>> decode(final DynamicOps<T> ops, final T input) {
      return isEmptyMap(ops, input)
         ? DataResult.success(Pair.of(Optional.empty(), input))
         : this.val$codec.decode(ops, input).map(pair -> pair.mapFirst(Optional::of));
   }

   private static <T> boolean isEmptyMap(final DynamicOps<T> ops, final T input) {
      Optional<MapLike<T>> map = ops.getMap(input).result();
      return map.isPresent() && map.get().entries().findAny().isEmpty();
   }

   public <T> DataResult<T> encode(final Optional<A> input, final DynamicOps<T> ops, final T prefix) {
      return input.isEmpty() ? DataResult.success(ops.emptyMap()) : this.val$codec.encode(input.get(), ops, prefix);
   }
}
