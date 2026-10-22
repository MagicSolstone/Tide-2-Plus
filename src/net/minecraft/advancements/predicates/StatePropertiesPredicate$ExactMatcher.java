package net.minecraft.advancements.predicates;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

record StatePropertiesPredicate$ExactMatcher(String value) implements StatePropertiesPredicate$ValueMatcher {
   public static final Codec<StatePropertiesPredicate$ExactMatcher> CODEC = Codec.STRING
      .xmap(StatePropertiesPredicate$ExactMatcher::new, StatePropertiesPredicate$ExactMatcher::value);
   public static final StreamCodec<ByteBuf, StatePropertiesPredicate$ExactMatcher> STREAM_CODEC = ByteBufCodecs.STRING_UTF8
      .map(StatePropertiesPredicate$ExactMatcher::new, StatePropertiesPredicate$ExactMatcher::value);

   @Override
   public <T extends Comparable<T>> boolean match(final StateHolder<?, ?> state, final Property<T> property) {
      T actualValue = state.getValue(property);
      Optional<T> typedExpected = property.getValue(this.value);
      return typedExpected.isPresent() && actualValue.compareTo(typedExpected.get()) == 0;
   }
}
