package net.minecraft.advancements.predicates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public record CollectionContentsPredicate$Multiple<T, P extends Predicate<T>>(List<P> tests) implements CollectionContentsPredicate<T, P> {
   public boolean test(final Iterable<? extends T> values) {
      List<Predicate<T>> testsToMatch = new ArrayList<>(this.tests);

      for (T value : values) {
         testsToMatch.removeIf(p -> p.test(value));
         if (testsToMatch.isEmpty()) {
            return true;
         }
      }

      return false;
   }

   @Override
   public List<P> unpack() {
      return this.tests;
   }
}
