package net.minecraft.core.component.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map.Entry;

public record DataComponentPredicate$Single<T extends DataComponentPredicate>(DataComponentPredicate$Type<T> type, T predicate) {
   private static <T extends DataComponentPredicate> MapCodec<DataComponentPredicate$Single<T>> wrapCodec(
      final DataComponentPredicate$Type<T> type, final Codec<T> codec
   ) {
      return RecordCodecBuilder.mapCodec(
         i -> i.group(codec.fieldOf("value").forGetter(DataComponentPredicate$Single::predicate))
            .apply(i, predicate -> new DataComponentPredicate$Single<>(type, (T)predicate))
      );
   }

   private static <T extends DataComponentPredicate> DataComponentPredicate$Single<T> fromEntry(final Entry<DataComponentPredicate$Type<?>, T> e) {
      return new DataComponentPredicate$Single<>((DataComponentPredicate$Type<T>)e.getKey(), e.getValue());
   }
}
