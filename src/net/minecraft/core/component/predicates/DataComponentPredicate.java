package net.minecraft.core.component.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public interface DataComponentPredicate {
   Codec<Map<DataComponentPredicate$Type<?>, DataComponentPredicate>> CODEC = Codec.dispatchedMap(
      DataComponentPredicate$Type.CODEC, DataComponentPredicate$Type::codec
   );
   StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate$Single<?>> SINGLE_STREAM_CODEC = DataComponentPredicate$Type.STREAM_CODEC
      .dispatch(DataComponentPredicate$Single::type, DataComponentPredicate$Type::singleStreamCodec);
   StreamCodec<RegistryFriendlyByteBuf, Map<DataComponentPredicate$Type<?>, DataComponentPredicate>> STREAM_CODEC = SINGLE_STREAM_CODEC.apply(
         ByteBufCodecs.list(64)
      )
      .map(
         singles -> singles.stream().collect(Collectors.toMap(DataComponentPredicate$Single::type, DataComponentPredicate$Single::predicate)),
         map -> map.entrySet().stream().map(DataComponentPredicate$Single::fromEntry).toList()
      );

   static MapCodec<DataComponentPredicate$Single<?>> singleCodec(final String name) {
      return DataComponentPredicate$Type.CODEC.dispatchMap(name, DataComponentPredicate$Single::type, DataComponentPredicate$Type::wrappedCodec);
   }

   boolean matches(DataComponentGetter components);
}
