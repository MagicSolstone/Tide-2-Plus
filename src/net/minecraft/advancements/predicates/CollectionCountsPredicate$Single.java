package net.minecraft.advancements.predicates;

import java.util.List;
import java.util.function.Predicate;

public record CollectionCountsPredicate$Single<T, P extends Predicate<T>>(CollectionCountsPredicate$Entry<T, P> entry)
   implements CollectionCountsPredicate<T, P> {
   public boolean test(final Iterable<? extends T> values) {
      return this.entry.test(values);
   }

   @Override
   public List<CollectionCountsPredicate$Entry<T, P>> unpack() {
      return List.of(this.entry);
   }
}
