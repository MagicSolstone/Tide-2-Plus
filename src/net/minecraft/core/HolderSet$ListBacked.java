package net.minecraft.core;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Stream;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;

public abstract class HolderSet$ListBacked<T> implements HolderSet<T> {
   protected abstract List<Holder<T>> contents();

   @Override
   public int size() {
      return this.contents().size();
   }

   @Override
   public Spliterator<Holder<T>> spliterator() {
      return this.contents().spliterator();
   }

   @Override
   public Iterator<Holder<T>> iterator() {
      return this.contents().iterator();
   }

   @Override
   public Stream<Holder<T>> stream() {
      return this.contents().stream();
   }

   @Override
   public Optional<Holder<T>> getRandomElement(final RandomSource random) {
      return Util.getRandomSafe(this.contents(), random);
   }

   @Override
   public Holder<T> get(final int index) {
      return this.contents().get(index);
   }

   @Override
   public boolean canSerializeIn(final HolderOwner<T> owner) {
      return true;
   }
}
