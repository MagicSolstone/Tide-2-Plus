package net.minecraft.advancements.predicates;

import java.util.List;
import java.util.function.Predicate;

public class CollectionContentsPredicate$Zero<T, P extends Predicate<T>> implements CollectionContentsPredicate<T, P> {
   public boolean test(final Iterable<? extends T> values) {
      return true;
   }

   @Override
   public List<P> unpack() {
      return List.of();
   }
}
