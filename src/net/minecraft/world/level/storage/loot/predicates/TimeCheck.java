package net.minecraft.world.level.storage.loot.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.level.storage.loot.IntRangePredicate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;

public record TimeCheck(Holder<WorldClock> clock, Optional<Long> period, IntRangePredicate value) implements LootItemCondition {
   public static final MapCodec<TimeCheck> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            WorldClock.CODEC.fieldOf("clock").forGetter(TimeCheck::clock),
            Codec.LONG.optionalFieldOf("period").forGetter(TimeCheck::period),
            IntRangePredicate.CODEC.fieldOf("value").forGetter(TimeCheck::value)
         )
         .apply(i, TimeCheck::new)
   );

   @Override
   public MapCodec<TimeCheck> codec() {
      return MAP_CODEC;
   }

   @Override
   public void validate(final ValidationContext context) {
      LootItemCondition.super.validate(context);
      Validatable.validate(context, "value", this.value);
   }

   public boolean test(final LootContext context) {
      ServerLevel level = context.getLevel();
      long time = level.clockManager().getInstance(this.clock).totalTicks();
      if (this.period.isPresent()) {
         time %= this.period.get();
      }

      return this.value.test(context, (int)time);
   }

   public static TimeCheck$Builder time(final Holder<WorldClock> clock, final IntRangePredicate value) {
      return new TimeCheck$Builder(clock, value);
   }
}
