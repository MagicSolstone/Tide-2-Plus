package net.minecraft.world.level.storage.loot;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import org.jspecify.annotations.Nullable;

public final class IntRangePredicate$Line implements IntRangePredicate {
   private final Optional<Holder<ContextIntProvider>> min;
   private final Optional<Holder<ContextIntProvider>> max;
   @Nullable
   private final IntRangePredicate$Line$IntChecker predicate;

   private IntRangePredicate$Line(final Optional<Holder<ContextIntProvider>> min, final Optional<Holder<ContextIntProvider>> max) {
      this.min = min;
      this.max = max;
      this.predicate = createPredicate(min.orElse(null), max.orElse(null));
   }

   @Nullable
   private static IntRangePredicate$Line$IntChecker createPredicate(
      @Nullable final Holder<ContextIntProvider> min, @Nullable final Holder<ContextIntProvider> max
   ) {
      if (min == null) {
         return max == null ? null : (context, input) -> input <= max.value().getInt(context);
      } else {
         return max == null
            ? (context, input) -> input >= min.value().getInt(context)
            : (context, input) -> input >= min.value().getInt(context) && input <= max.value().getInt(context);
      }
   }

   @Override
   public void validate(final ValidationContext context) {
      Validatable.validateHolder(context, "min", this.min);
      Validatable.validateHolder(context, "max", this.max);
   }

   @Override
   public boolean test(final LootContext context, final int input) {
      return this.predicate == null || this.predicate.test(context, input);
   }

   @Override
   public boolean test(final LootContext context, final ContextIntProvider input) {
      return this.predicate == null || this.predicate.test(context, input.getInt(context));
   }
}
