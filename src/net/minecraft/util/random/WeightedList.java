package net.minecraft.util.random;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public final class WeightedList<E> {
   private static final int FLAT_THRESHOLD = 64;
   private final int totalWeight;
   private final List<Weighted<E>> items;
   @Nullable
   private final WeightedList$Selector<E> selector;

   private WeightedList(final List<? extends Weighted<E>> items) {
      this.items = List.copyOf(items);
      this.totalWeight = WeightedRandom.getTotalWeight(items, Weighted::weight);
      if (this.totalWeight == 0) {
         this.selector = null;
      } else if (this.totalWeight < 64) {
         this.selector = new WeightedList$Flat<>(this.items, this.totalWeight);
      } else {
         this.selector = new WeightedList$Compact<>(this.items);
      }
   }

   public static <E> WeightedList<E> of() {
      return new WeightedList<>(List.of());
   }

   public static <E> WeightedList<E> of(final E value) {
      return new WeightedList<>(List.of(new Weighted<>(value, 1)));
   }

   public static <E> WeightedList<E> of(final E... items) {
      WeightedList$Builder<E> builder = builder();

      for (E item : items) {
         builder.add(item);
      }

      return builder.build();
   }

   @SafeVarargs
   public static <E> WeightedList<E> of(final Weighted<E>... items) {
      return new WeightedList<>(List.of(items));
   }

   public static <E> WeightedList<E> of(final List<Weighted<E>> items) {
      return new WeightedList<>(items);
   }

   public static <E> WeightedList$Builder<E> builder() {
      return new WeightedList$Builder<>();
   }

   public boolean isEmpty() {
      return this.selector == null;
   }

   public <T> WeightedList<T> map(final Function<E, T> mapper) {
      return new WeightedList(Lists.transform(this.items, e -> e.map(mapper)));
   }

   public Optional<E> getRandom(final RandomSource random) {
      if (this.selector == null) {
         return Optional.empty();
      } else {
         int selection = random.nextInt(this.totalWeight);
         return Optional.of(this.selector.get(selection));
      }
   }

   public E getRandomOrThrow(final RandomSource random) {
      if (this.selector == null) {
         throw new IllegalStateException("Weighted list has no elements");
      } else {
         int selection = random.nextInt(this.totalWeight);
         return this.selector.get(selection);
      }
   }

   public List<Weighted<E>> unwrap() {
      return this.items;
   }

   private static <E> Codec<WeightedList<E>> entryToListCodec(final Codec<Weighted<E>> weightedElementCodec) {
      return weightedElementCodec.listOf().xmap(WeightedList::of, WeightedList::unwrap);
   }

   public static <E> Codec<WeightedList<E>> codec(final Codec<E> elementCodec) {
      return entryToListCodec(Weighted.codec(elementCodec));
   }

   public static <E> Codec<WeightedList<E>> codec(final MapCodec<E> elementCodec) {
      return entryToListCodec(Weighted.codec(elementCodec));
   }

   private static <E> Codec<WeightedList<E>> entryToNonEmptyListCodec(final Codec<Weighted<E>> weightedElementCodec) {
      return entryToListCodec(weightedElementCodec)
         .validate(
            list -> list.isEmpty() ? DataResult.error(() -> "Weighted list must contain at least one entry with non-zero weight") : DataResult.success(list)
         );
   }

   public static <E> Codec<WeightedList<E>> nonEmptyCodec(final Codec<E> elementCodec) {
      return entryToNonEmptyListCodec(Weighted.codec(elementCodec));
   }

   public static <E> Codec<WeightedList<E>> nonEmptyCodec(final MapCodec<E> elementCodec) {
      return entryToNonEmptyListCodec(Weighted.codec(elementCodec));
   }

   public static <E, B extends ByteBuf> StreamCodec<B, WeightedList<E>> streamCodec(final StreamCodec<B, E> elementCodec) {
      return Weighted.streamCodec(elementCodec).apply(ByteBufCodecs.list()).map(WeightedList::of, WeightedList::unwrap);
   }

   public boolean contains(final E value) {
      for (Weighted<E> item : this.items) {
         if (item.value().equals(value)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public boolean equals(@Nullable final Object obj) {
      if (this == obj) {
         return true;
      } else {
         return !(obj instanceof WeightedList<?> list) ? false : this.totalWeight == list.totalWeight && Objects.equals(this.items, list.items);
      }
   }

   @Override
   public int hashCode() {
      int result = this.totalWeight;
      return 31 * result + this.items.hashCode();
   }
}
