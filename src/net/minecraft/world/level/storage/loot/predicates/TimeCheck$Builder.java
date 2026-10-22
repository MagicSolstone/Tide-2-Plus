package net.minecraft.world.level.storage.loot.predicates;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.level.storage.loot.IntRangePredicate;

public class TimeCheck$Builder implements LootItemCondition$Builder {
   private final Holder<WorldClock> clock;
   private Optional<Long> period = Optional.empty();
   private final IntRangePredicate value;

   public TimeCheck$Builder(final Holder<WorldClock> clock, final IntRangePredicate value) {
      this.clock = clock;
      this.value = value;
   }

   public TimeCheck$Builder setPeriod(final long period) {
      this.period = Optional.of(period);
      return this;
   }

   public TimeCheck build() {
      return new TimeCheck(this.clock, this.period, this.value);
   }
}
