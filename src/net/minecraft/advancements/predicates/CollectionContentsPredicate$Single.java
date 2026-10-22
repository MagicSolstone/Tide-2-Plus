package net.minecraft.advancements.predicates;

import java.util.List;
import java.util.function.Predicate;

public record CollectionContentsPredicate$Single<T, P extends Predicate<T>>(P test) implements CollectionContentsPredicate<T, P> {
   public boolean test(final Iterable<? extends T> values) {
      for (T value : values) {
         if (this.test.test(value)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public List<P> unpack() {
      return List.of(this.test);
   }
}
