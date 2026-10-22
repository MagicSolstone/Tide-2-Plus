package net.minecraft.advancements.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Predicate;

public record CollectionCountsPredicate$Entry<T, P extends Predicate<T>>(P test, MinMaxBounds$Ints count) {
   public static <T, P extends Predicate<T>> Codec<CollectionCountsPredicate$Entry<T, P>> codec(final Codec<P> elementCodec) {
      return RecordCodecBuilder.create(
         i -> i.group(
               elementCodec.fieldOf("test").forGetter(CollectionCountsPredicate$Entry::test),
               MinMaxBounds$Ints.CODEC.fieldOf("count").forGetter(CollectionCountsPredicate$Entry::count)
            )
            .apply(i, CollectionCountsPredicate$Entry::new)
      );
   }

   public boolean test(final Iterable<? extends T> values) {
      int count = 0;

      for (T value : values) {
         if (this.test.test(value)) {
            count++;
         }
      }

      return this.count.matches(count);
   }
}
