package net.minecraft.world.entity.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;

public record PriorityProvider$Selector<Context, Condition extends PriorityProvider$SelectorCondition<Context>>(Optional<Condition> condition, int priority) {
   public PriorityProvider$Selector(final Condition condition, final int priority) {
      this(Optional.of(condition), priority);
   }

   public PriorityProvider$Selector(final int priority) {
      this(Optional.empty(), priority);
   }

   public static <Context, Condition extends PriorityProvider$SelectorCondition<Context>> Codec<PriorityProvider$Selector<Context, Condition>> codec(
      final Codec<Condition> conditionCodec
   ) {
      return RecordCodecBuilder.create(
         i -> i.group(
               conditionCodec.optionalFieldOf("condition").forGetter(PriorityProvider$Selector::condition),
               Codec.INT.fieldOf("priority").forGetter(PriorityProvider$Selector::priority)
            )
            .apply(i, PriorityProvider$Selector::new)
      );
   }
}
