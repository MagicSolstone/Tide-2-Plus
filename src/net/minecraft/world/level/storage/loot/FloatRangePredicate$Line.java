package net.minecraft.world.level.storage.loot;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import org.jspecify.annotations.Nullable;

public final class FloatRangePredicate$Line implements FloatRangePredicate {
   private final Optional<Holder<ContextFloatProvider>> min;
   private final Optional<Holder<ContextFloatProvider>> max;
   @Nullable
   private final FloatRangePredicate$Line$FloatChecker predicate;

   private FloatRangePredicate$Line(final Optional<Holder<ContextFloatProvider>> min, final Optional<Holder<ContextFloatProvider>> max) {
      this.min = min;
      this.max = max;
      this.predicate = createPredicate(min.orElse(null), max.orElse(null));
   }

   @Nullable
   private static FloatRangePredicate$Line$FloatChecker createPredicate(
      @Nullable final Holder<ContextFloatProvider> min, @Nullable final Holder<ContextFloatProvider> max
   ) {
      if (min == null) {
         return max == null ? null : (context, input) -> input <= max.value().getFloat(context);
      } else {
         return max == null
            ? (context, input) -> input >= min.value().getFloat(context)
            : (context, input) -> input >= min.value().getFloat(context) && input <= max.value().getFloat(context);
      }
   }

   @Override
   public void validate(final ValidationContext context) {
      Validatable.validateHolder(context, "min", this.min);
      Validatable.validateHolder(context, "max", this.max);
   }

   @Override
   public boolean test(final LootContext context, final float input) {
      return this.predicate == null || this.predicate.test(context, input);
   }

   @Override
   public boolean test(final LootContext context, final ContextFloatProvider input) {
      return this.predicate == null || this.predicate.test(context, input.getFloat(context));
   }
}
