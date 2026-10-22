package net.minecraft.world.level.storage.loot;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;

public record FloatRangePredicate$Point(Holder<ContextFloatProvider> value) implements FloatRangePredicate {
   @Override
   public void validate(final ValidationContext context) {
      Validatable.validateHolder(context, this.value);
   }

   private float computeValue(final LootContext context) {
      return this.value.value().getFloat(context);
   }

   @Override
   public boolean test(final LootContext context, final float input) {
      return this.computeValue(context) == input;
   }
}
