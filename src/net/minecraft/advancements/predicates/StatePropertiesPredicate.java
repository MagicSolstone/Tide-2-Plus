package net.minecraft.advancements.predicates;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.material.FluidState;

public record StatePropertiesPredicate(List<StatePropertiesPredicate$PropertyMatcher> properties) {
   private static final Codec<List<StatePropertiesPredicate$PropertyMatcher>> PROPERTIES_CODEC = Codec.unboundedMap(
         Codec.STRING, StatePropertiesPredicate$ValueMatcher.CODEC
      )
      .xmap(
         map -> map.entrySet()
            .stream()
            .map(entry -> new StatePropertiesPredicate$PropertyMatcher((String)entry.getKey(), (StatePropertiesPredicate$ValueMatcher)entry.getValue()))
            .toList(),
         properties -> properties.stream()
            .collect(Collectors.toMap(StatePropertiesPredicate$PropertyMatcher::name, StatePropertiesPredicate$PropertyMatcher::valueMatcher))
      );
   public static final Codec<StatePropertiesPredicate> CODEC = PROPERTIES_CODEC.xmap(StatePropertiesPredicate::new, StatePropertiesPredicate::properties);
   public static final StreamCodec<ByteBuf, StatePropertiesPredicate> STREAM_CODEC = StatePropertiesPredicate$PropertyMatcher.STREAM_CODEC
      .apply(ByteBufCodecs.list())
      .map(StatePropertiesPredicate::new, StatePropertiesPredicate::properties);

   public <S extends StateHolder<?, S>> boolean matches(final StateDefinition<?, S> definition, final S state) {
      for (StatePropertiesPredicate$PropertyMatcher matcher : this.properties) {
         if (!matcher.match(definition, state)) {
            return false;
         }
      }

      return true;
   }

   public boolean matches(final BlockState state) {
      return this.matches(state.getBlock().getStateDefinition(), state);
   }

   public boolean matches(final FluidState state) {
      return this.matches(state.getType().getStateDefinition(), state);
   }

   public Optional<String> checkState(final StateDefinition<?, ?> states) {
      for (StatePropertiesPredicate$PropertyMatcher property : this.properties) {
         Optional<String> unknownProperty = property.checkState(states);
         if (unknownProperty.isPresent()) {
            return unknownProperty;
         }
      }

      return Optional.empty();
   }
}
