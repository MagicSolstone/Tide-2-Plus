package net.minecraft.advancements.predicates;

import java.util.List;
import java.util.function.Predicate;

public record CollectionCountsPredicate$Multiple<T, P extends Predicate<T>>(List<CollectionCountsPredicate$Entry<T, P>> entries)
   implements CollectionCountsPredicate<T, P> {
   public boolean test(final Iterable<? extends T> values) {
      for (CollectionCountsPredicate$Entry<T, P> entry : this.entries) {
         if (!entry.test(values)) {
            return false;
         }
      }

      return true;
   }

   @Override
   public List<CollectionCountsPredicate$Entry<T, P>> unpack() {
      return this.entries;
   }
}
