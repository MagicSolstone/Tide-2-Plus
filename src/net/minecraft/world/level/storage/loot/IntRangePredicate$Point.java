package net.minecraft.world.level.storage.loot;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

public record IntRangePredicate$Point(Holder<ContextIntProvider> value) implements IntRangePredicate {
   @Override
   public void validate(final ValidationContext context) {
      Validatable.validateHolder(context, this.value);
   }

   private int computeValue(final LootContext context) {
      return this.value.value().getInt(context);
   }

   @Override
   public boolean test(final LootContext context, final int input) {
      return this.computeValue(context) == input;
   }
}
