package net.minecraft.core;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.datafixers.util.Either;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;

public interface HolderSet<T> extends Iterable<Holder<T>> {
   Stream<Holder<T>> stream();

   int size();

   boolean isBound();

   Either<TagKey<T>, List<Holder<T>>> unwrap();

   Optional<Holder<T>> getRandomElement(RandomSource random);

   Holder<T> get(int index);

   boolean contains(final Holder<T> value);

   boolean canSerializeIn(HolderOwner<T> owner);

   Optional<TagKey<T>> unwrapKey();

   @Deprecated
   @VisibleForTesting
   static <T> HolderSet$Named<T> emptyNamed(final HolderOwner<T> owner, final TagKey<T> key) {
      return new HolderSet$1(owner, key);
   }

   static <T> HolderSet<T> empty() {
      return (HolderSet<T>)HolderSet$Direct.EMPTY;
   }

   @SafeVarargs
   static <T> HolderSet$Direct<T> direct(final Holder<T>... values) {
      return new HolderSet$Direct<>(List.of(values));
   }

   static <T> HolderSet$Direct<T> direct(final List<? extends Holder<T>> values) {
      return new HolderSet$Direct<>(List.copyOf(values));
   }

   @SafeVarargs
   static <E, T> HolderSet$Direct<T> direct(final Function<E, Holder<T>> holderGetter, final E... elements) {
      return direct(Stream.of(elements).map(holderGetter).toList());
   }

   static <E, T> HolderSet$Direct<T> direct(final Function<E, Holder<T>> holderGetter, final Collection<E> elements) {
      return direct(elements.stream().map(holderGetter).toList());
   }
}
