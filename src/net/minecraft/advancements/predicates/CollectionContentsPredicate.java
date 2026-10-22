package net.minecraft.advancements.predicates;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.Predicate;

public interface CollectionContentsPredicate<T, P extends Predicate<T>> extends Predicate<Iterable<? extends T>> {
   List<P> unpack();

   static <T, P extends Predicate<T>> Codec<CollectionContentsPredicate<T, P>> codec(final Codec<P> elementCodec) {
      return elementCodec.listOf().xmap(CollectionContentsPredicate::of, CollectionContentsPredicate::unpack);
   }

   @SafeVarargs
   static <T, P extends Predicate<T>> CollectionContentsPredicate<T, P> of(final P... predicates) {
      return of(List.of(predicates));
   }

   static <T, P extends Predicate<T>> CollectionContentsPredicate<T, P> of(final List<P> predicates) {
      return (CollectionContentsPredicate<T, P>)(switch (predicates.size()) {
         case 0 -> new CollectionContentsPredicate$Zero();
         case 1 -> new CollectionContentsPredicate$Single(predicates.getFirst());
         default -> new CollectionContentsPredicate$Multiple(predicates);
      });
   }
}
