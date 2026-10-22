package net.minecraft.world.level.storage.loot;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public sealed interface IntRangePredicate extends Validatable permits IntRangePredicate$Point, IntRangePredicate$Line {
   Codec<IntRangePredicate$Point> POINT_CODEC = ContextIntProviders.CODEC.xmap(IntRangePredicate$Point::new, IntRangePredicate$Point::value);
   Codec<IntRangePredicate$Line> LINE_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ContextIntProviders.CODEC.optionalFieldOf("min").forGetter(r -> r.min), ContextIntProviders.CODEC.optionalFieldOf("max").forGetter(r -> r.max)
         )
         .apply(i, IntRangePredicate$Line::new)
   );
   Codec<IntRangePredicate> CODEC = Codec.either(POINT_CODEC, LINE_CODEC).xmap(Either::unwrap, range -> {
      return switch (range) {
         case IntRangePredicate$Point point -> Either.left(point);
         case IntRangePredicate$Line line -> Either.right(line);
         default -> throw new MatchException(null, null);
      };
   });

   static IntRangePredicate exact(final int value) {
      return new IntRangePredicate$Point(ContextIntProviders.exactly(value));
   }

   static IntRangePredicate range(final int min, final int max) {
      return new IntRangePredicate$Line(Optional.of(ContextIntProviders.exactly(min)), Optional.of(ContextIntProviders.exactly(max)));
   }

   static IntRangePredicate lowerBound(final int value) {
      return new IntRangePredicate$Line(Optional.of(ContextIntProviders.exactly(value)), Optional.empty());
   }

   static IntRangePredicate upperBound(final int value) {
      return new IntRangePredicate$Line(Optional.empty(), Optional.of(ContextIntProviders.exactly(value)));
   }

   boolean test(LootContext context, int input);

   default boolean test(final LootContext context, final ContextIntProvider input) {
      return this.test(context, input.getInt(context));
   }
}
