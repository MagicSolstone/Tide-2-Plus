package net.minecraft.advancements.predicates;

import com.google.common.collect.Range;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.DataResult;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.network.chat.Component;

public interface MinMaxBounds<T extends Number & Comparable<T>> {
   SimpleCommandExceptionType ERROR_EMPTY = new SimpleCommandExceptionType(Component.translatable("argument.range.empty"));
   SimpleCommandExceptionType ERROR_SWAPPED = new SimpleCommandExceptionType(Component.translatable("argument.range.swapped"));

   MinMaxBounds$Bounds<T> bounds();

   default Optional<T> min() {
      return this.bounds().min;
   }

   default Optional<T> max() {
      return this.bounds().max;
   }

   default boolean isAny() {
      return this.bounds().isAny();
   }

   static <V extends Number & Comparable<V>, B extends MinMaxBounds<V>> Function<B, DataResult<B>> validateContainedInRange(final MinMaxBounds<V> allowed) {
      Range<V> allowedRange = allowed.bounds().asRange();
      return target -> {
         Range<V> selfAsRange = target.bounds().asRange();
         return !allowedRange.encloses(selfAsRange)
            ? DataResult.error(() -> "Range must be within " + allowedRange + ", but was " + selfAsRange)
            : DataResult.success(target);
      };
   }
}
