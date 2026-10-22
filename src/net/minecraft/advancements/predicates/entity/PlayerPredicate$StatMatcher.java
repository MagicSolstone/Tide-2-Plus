package net.minecraft.advancements.predicates.entity;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Supplier;
import net.minecraft.advancements.predicates.MinMaxBounds$Ints;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.StatsCounter;

record PlayerPredicate$StatMatcher<T>(StatType<T> type, Holder<T> value, MinMaxBounds$Ints range, Supplier<Stat<T>> stat) {
   public static final Codec<PlayerPredicate$StatMatcher<?>> CODEC = BuiltInRegistries.STAT_TYPE
      .byNameCodec()
      .dispatch(PlayerPredicate$StatMatcher::type, PlayerPredicate$StatMatcher::createTypedCodec);

   public PlayerPredicate$StatMatcher(final StatType<T> type, final Holder<T> value, final MinMaxBounds$Ints range) {
      this(type, value, range, Suppliers.memoize(() -> type.get(value.value())));
   }

   private static <T> MapCodec<PlayerPredicate$StatMatcher<T>> createTypedCodec(final StatType<T> type) {
      return RecordCodecBuilder.mapCodec(
         i -> i.group(
               type.getRegistry().holderByNameCodec().fieldOf("stat").forGetter(PlayerPredicate$StatMatcher::value),
               MinMaxBounds$Ints.CODEC.optionalFieldOf("value", MinMaxBounds$Ints.ANY).forGetter(PlayerPredicate$StatMatcher::range)
            )
            .apply(i, (value, range) -> new PlayerPredicate$StatMatcher<>(type, value, range))
      );
   }

   public boolean matches(final StatsCounter counter) {
      return this.range.matches(counter.getValue(this.stat.get()));
   }
}
