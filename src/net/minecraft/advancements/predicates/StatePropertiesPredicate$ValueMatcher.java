package net.minecraft.advancements.predicates;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

interface StatePropertiesPredicate$ValueMatcher {
   Codec<StatePropertiesPredicate$ValueMatcher> CODEC = Codec.either(StatePropertiesPredicate$ExactMatcher.CODEC, StatePropertiesPredicate$RangedMatcher.CODEC)
      .xmap(Either::unwrap, matcher -> {
         if (matcher instanceof StatePropertiesPredicate$ExactMatcher exact) {
            return Either.left(exact);
         } else if (matcher instanceof StatePropertiesPredicate$RangedMatcher ranged) {
            return Either.right(ranged);
         } else {
            throw new UnsupportedOperationException();
         }
      });
   StreamCodec<ByteBuf, StatePropertiesPredicate$ValueMatcher> STREAM_CODEC = ByteBufCodecs.either(
         StatePropertiesPredicate$ExactMatcher.STREAM_CODEC, StatePropertiesPredicate$RangedMatcher.STREAM_CODEC
      )
      .map(Either::unwrap, matcher -> {
         if (matcher instanceof StatePropertiesPredicate$ExactMatcher exact) {
            return Either.left(exact);
         } else if (matcher instanceof StatePropertiesPredicate$RangedMatcher ranged) {
            return Either.right(ranged);
         } else {
            throw new UnsupportedOperationException();
         }
      });

   <T extends Comparable<T>> boolean match(StateHolder<?, ?> state, Property<T> property);
}
