package net.minecraft.advancements.predicates;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.Predicate;

public interface CollectionCountsPredicate<T, P extends Predicate<T>> extends Predicate<Iterable<? extends T>> {
   List<CollectionCountsPredicate$Entry<T, P>> unpack();

   static <T, P extends Predicate<T>> Codec<CollectionCountsPredicate<T, P>> codec(final Codec<P> elementCodec) {
      return CollectionCountsPredicate$Entry.codec(elementCodec).listOf().xmap(CollectionCountsPredicate::of, CollectionCountsPredicate::unpack);
   }

   @SafeVarargs
   static <T, P extends Predicate<T>> CollectionCountsPredicate<T, P> of(final CollectionCountsPredicate$Entry<T, P>... predicates) {
      return of(List.of(predicates));
   }

   static <T, P extends Predicate<T>> CollectionCountsPredicate<T, P> of(final List<CollectionCountsPredicate$Entry<T, P>> predicates) {
      return (CollectionCountsPredicate<T, P>)(switch (predicates.size()) {
         case 0 -> new CollectionCountsPredicate$Zero();
         case 1 -> new CollectionCountsPredicate$Single(predicates.getFirst());
         default -> new CollectionCountsPredicate$Multiple(predicates);
      });
   }
}
