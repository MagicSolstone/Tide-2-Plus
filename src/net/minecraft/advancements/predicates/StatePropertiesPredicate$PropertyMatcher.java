package net.minecraft.advancements.predicates;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

record StatePropertiesPredicate$PropertyMatcher(String name, StatePropertiesPredicate$ValueMatcher valueMatcher) {
   public static final StreamCodec<ByteBuf, StatePropertiesPredicate$PropertyMatcher> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8,
      StatePropertiesPredicate$PropertyMatcher::name,
      StatePropertiesPredicate$ValueMatcher.STREAM_CODEC,
      StatePropertiesPredicate$PropertyMatcher::valueMatcher,
      StatePropertiesPredicate$PropertyMatcher::new
   );

   public <S extends StateHolder<?, S>> boolean match(final StateDefinition<?, S> definition, final S state) {
      Property<?> property = definition.getProperty(this.name);
      return property != null && this.valueMatcher.match(state, property);
   }

   public Optional<String> checkState(final StateDefinition<?, ?> states) {
      Property<?> property = states.getProperty(this.name);
      return property != null ? Optional.empty() : Optional.of(this.name);
   }
}
