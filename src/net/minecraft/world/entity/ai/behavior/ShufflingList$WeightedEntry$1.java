package net.minecraft.world.entity.ai.behavior;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;

class ShufflingList$WeightedEntry$1 implements Codec<ShufflingList$WeightedEntry<E>> {
   ShufflingList$WeightedEntry$1(final Codec val$elementCodec) {
      this.val$elementCodec = val$elementCodecx;
   }

   public <T> DataResult<Pair<ShufflingList$WeightedEntry<E>, T>> decode(final DynamicOps<T> ops, final T input) {
      Dynamic<T> map = new Dynamic(ops, input);
      return map.get("data")
         .flatMap(this.val$elementCodec::parse)
         .map(data -> new ShufflingList$WeightedEntry<>(data, map.get("weight").asInt(1)))
         .map(r -> Pair.of(r, ops.empty()));
   }

   public <T> DataResult<T> encode(final ShufflingList$WeightedEntry<E> input, final DynamicOps<T> ops, final T prefix) {
      return ops.mapBuilder().add("weight", ops.createInt(input.weight)).add("data", this.val$elementCodec.encodeStart(ops, input.data)).build(prefix);
   }
}
