package net.minecraft.world.level.storage.loot;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;

public sealed interface FloatRangePredicate extends Validatable permits FloatRangePredicate$Point, FloatRangePredicate$Line {
   Codec<FloatRangePredicate$Point> POINT_CODEC = ContextFloatProviders.CODEC.xmap(FloatRangePredicate$Point::new, FloatRangePredicate$Point::value);
   Codec<FloatRangePredicate$Line> LINE_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ContextFloatProviders.CODEC.optionalFieldOf("min").forGetter(r -> r.min), ContextFloatProviders.CODEC.optionalFieldOf("max").forGetter(r -> r.max)
         )
         .apply(i, FloatRangePredicate$Line::new)
   );
   Codec<FloatRangePredicate> CODEC = Codec.either(POINT_CODEC, LINE_CODEC).xmap(Either::unwrap, range -> {
      return switch (range) {
         case FloatRangePredicate$Point point -> Either.left(point);
         case FloatRangePredicate$Line line -> Either.right(line);
         default -> throw new MatchException(null, null);
      };
   });

   static FloatRangePredicate exact(final float value) {
      return new FloatRangePredicate$Point(ContextFloatProviders.exactly(value));
   }

   static FloatRangePredicate range(final float min, final float max) {
      return new FloatRangePredicate$Line(Optional.of(ContextFloatProviders.exactly(min)), Optional.of(ContextFloatProviders.exactly(max)));
   }

   static FloatRangePredicate lowerBound(final float value) {
      return new FloatRangePredicate$Line(Optional.of(ContextFloatProviders.exactly(value)), Optional.empty());
   }

   static FloatRangePredicate upperBound(final float value) {
      return new FloatRangePredicate$Line(Optional.empty(), Optional.of(ContextFloatProviders.exactly(value)));
   }

   boolean test(LootContext context, float input);

   default boolean test(final LootContext context, final ContextFloatProvider input) {
      return this.test(context, input.getFloat(context));
   }
}
