package net.minecraft.advancements.predicates;

import java.util.List;
import java.util.function.Predicate;

public class CollectionCountsPredicate$Zero<T, P extends Predicate<T>> implements CollectionCountsPredicate<T, P> {
   public boolean test(final Iterable<? extends T> values) {
      return true;
   }

   @Override
   public List<CollectionCountsPredicate$Entry<T, P>> unpack() {
      return List.of();
   }
}
