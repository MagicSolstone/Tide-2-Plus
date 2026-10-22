package net.minecraft.advancements.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

record StatePropertiesPredicate$RangedMatcher(Optional<String> minValue, Optional<String> maxValue) implements StatePropertiesPredicate$ValueMatcher {
   public static final Codec<StatePropertiesPredicate$RangedMatcher> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.STRING.optionalFieldOf("min").forGetter(StatePropertiesPredicate$RangedMatcher::minValue),
            Codec.STRING.optionalFieldOf("max").forGetter(StatePropertiesPredicate$RangedMatcher::maxValue)
         )
         .apply(i, StatePropertiesPredicate$RangedMatcher::new)
   );
   public static final StreamCodec<ByteBuf, StatePropertiesPredicate$RangedMatcher> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
      StatePropertiesPredicate$RangedMatcher::minValue,
      ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
      StatePropertiesPredicate$RangedMatcher::maxValue,
      StatePropertiesPredicate$RangedMatcher::new
   );

   @Override
   public <T extends Comparable<T>> boolean match(final StateHolder<?, ?> state, final Property<T> property) {
      T value = state.getValue(property);
      if (this.minValue.isPresent()) {
         Optional<T> typedMinValue = property.getValue(this.minValue.get());
         if (typedMinValue.isEmpty() || value.compareTo(typedMinValue.get()) < 0) {
            return false;
         }
      }

      if (this.maxValue.isPresent()) {
         Optional<T> typedMaxValue = property.getValue(this.maxValue.get());
         if (typedMaxValue.isEmpty() || value.compareTo(typedMaxValue.get()) > 0) {
            return false;
         }
      }

      return true;
   }
}
